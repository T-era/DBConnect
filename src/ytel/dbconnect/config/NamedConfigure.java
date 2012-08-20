package ytel.dbconnect.config;

class NamedConfigure {
	private final String name;
	private final UserConfigure config;

	NamedConfigure(String name, UserConfigure config) {
		this.name = name;
		this.config = config;
	}

	String getName() {
		return name;
	}
	UserConfigure getConfigure() {
		return config;
	}

	@Override
	public String toString() {
		return name;
	}
}
