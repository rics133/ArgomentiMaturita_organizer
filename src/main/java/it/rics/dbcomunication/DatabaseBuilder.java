package it.rics.dbcomunication;

public final class DatabaseBuilder {
  private String Username;
  private String Password;

  private DatabaseBuilder() {
  }

  public static DatabaseBuilder newDatabase() {
    return new DatabaseBuilder();
  }

  public DatabaseBuilder withUsername(String Username) {
    this.Username = Username;
    return this;
  }

  public DatabaseBuilder withPassword(String Password) {
    this.Password = Password;
    return this;
  }

  public Database build() {
    Database database = new Database();
    database.setUsername(Username);
    database.setPassword(Password);
    return database;
  }
}
