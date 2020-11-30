package fusion.domain

case class AppDetails(name: String)
case class DbConnDetails(driver: String, url: String, user: String, password: String)
case class AppConfig(app: AppDetails, db: DbConnDetails)
