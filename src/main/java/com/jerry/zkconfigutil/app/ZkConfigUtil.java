package com.jerry.zkconfigutil.app;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;

import com.jerry.zkconfigutil.annotation.FieldZkConfigurable;
import com.jerry.zkconfigutil.exception.NotRegistedException;
import com.jerry.zkconfigutil.help.SignalHelper;
import com.jerry.zkconfigutil.resolve.AbstractResolve;
import com.jerry.zkconfigutil.resolve.ReflectResolve;
import com.jerry.zkconfigutil.resolve.Resolve;
import com.jerry.zkconfigutil.util.Updater;
import com.jerry.zkconfigutil.zkserializer.StringZkSerializer;

public final class ZkConfigUtil implements IZkDataListener {

	private final Logger logger = Logger.getLogger(ZkConfigUtil.class);
	private final String globalZkServer;
	private final String globalPath;

	public ZkConfigUtil(String globalZkServer) {
		this.globalZkServer = globalZkServer;
		this.globalPath = "/";
	}

	public ZkConfigUtil(String globalZkServer, String globalPath) {
		this.globalZkServer = globalZkServer;
		this.globalPath = globalPath;
	}

	public final synchronized void register(Class<?> cla, boolean isCreateIfNUll)
			throws NotRegistedException, InstantiationException,
			IllegalAccessException {
		this.register(cla, isCreateIfNUll, this.globalPath);
	}

	public final synchronized void register(Class<?> cla,
			boolean isCreateIfNUll, String rootPath)
			throws NotRegistedException, InstantiationException,
			IllegalAccessException {
		// if (!cla.isAnnotationPresent(TypeZkConfigurable.class)) {
		// throw new NotRegistedException();
		// }
		final ZkClient zkClient;
		if ("".equals(this.globalZkServer)) {
			logger.error("please set globalZkServer or set typeZkConfigurable.useOwnZkServer()=false to use own zkserver system will exit!!!");
			System.exit(0);
		}
		zkClient = this.makeZkClient(this.globalZkServer);
		// String packagePath = cla.getPackage().getName();
		// packagePath = packagePath.replaceAll("\\.", "/");
		//
		// rootPath = this.makeZkPath(rootPath, packagePath);
		String path = this.makeZkPath(rootPath, cla.getName());

		path = path.replace("$", "/"); // inclass

		final Field[] fields = cla.getDeclaredFields();

		for (Field field : fields) {
			if (!field.isAnnotationPresent(FieldZkConfigurable.class))
				continue;
			logger.debug("field : " + field.getName() + "   type : "
					+ field.getType().getSimpleName());
			FieldZkConfigurable fieldZkConfigurable = field
					.getAnnotation(FieldZkConfigurable.class);

			final String fieldPath = this.makeZkPath(path, field.getName());

			String value = zkClient.readData(fieldPath, true);
			logger.debug(fieldPath + " : " + value);

			Class<? extends AbstractResolve> resolve = fieldZkConfigurable
					.resolve();
			Resolve resolveInstance;
			if (resolve == ReflectResolve.class) {
				resolveInstance = new ReflectResolve(cla, field);
			} else {
				resolveInstance = resolve.newInstance();
			}

			/**
			 * Dosen't have value
			 */
			if (value == null && !isCreateIfNUll) {
				continue;
			} else if (value == null && isCreateIfNUll) {
				zkClient.createPersistent(fieldPath, true);
				String defaultValue = resolveInstance.resolve();
				zkClient.writeData(fieldPath, defaultValue);
			} else {
				/**
				 * have value
				 */
				resolveInstance.dResolve(value);
			}

			/**
			 * for USR2 signal
			 */
			SignalHelper.mark(cla, fieldPath, resolveInstance,
					fieldZkConfigurable.dynamicUpdate());

			if (fieldZkConfigurable.dynamicUpdate()) {
				logger.debug("dynamicUpdate " + fieldPath);
				zkClient.subscribeDataChanges(fieldPath, this);
				Updater.register(fieldPath, resolveInstance);
			}
		}
	}

	private final Map<String, ZkClient> zkClientCache = new HashMap<String, ZkClient>(
			4);
	private final StringZkSerializer stringZkSerializer = new StringZkSerializer();

	private final ZkClient makeZkClient(String server) {

		if (this.zkClientCache.containsKey(server))
			return this.zkClientCache.get(server);

		final ZkClient zkClient = new ZkClient(server, 30000, 30000,
				this.stringZkSerializer);
		this.zkClientCache.put(server, zkClient);
		return zkClient;
	}

	private final String makeZkPath(String parent, String pathName) {
		final String separator = "/";
		if (!parent.startsWith(separator)) {
			parent = separator + parent;
		}
		if (!parent.endsWith(separator)) {
			parent = parent + separator;
		}
		if (pathName.startsWith(separator)) {
			pathName = pathName.substring(1);
		}
		return parent + pathName;
	}

	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
		logger.warn("change event : " + dataPath + " : " + data.toString());
		Updater.update(dataPath, data.toString());
	}

	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		logger.warn("delete event : " + dataPath);

	}

}
