package com.example.testingmysql

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

/**
 * Connector class to create a database connection through an SSH tunnel.
 * User must enter their SSH username and password in the class variables
 */
object Connector {

    private const val dbPort: Int = 3306 // the port hosting the DB
    private const val localPort: Int = 1111 // the local port (random) to forward the connection to
    private const val sshPort: Int = 22 // the port to ssh into

    // Your SSH username and password - DO NOT commit these changes.
    // TODO: Consider adding `Connector.kt` to .gitignore
    private const val sshUsername: String = ""
    private const val sshPassword: String = ""

    private const val dbName: String = "csc2033_team24"
    private const val dbUsername: String = "csc2033_team24"

    // Your database password
    private const val dbPassword: String = ""

    private const val dbHost: String = "cs-db.ncl.ac.uk"
    private const val sshHost: String = "linux.cs.ncl.ac.uk"

    // this will be the new db address to connect to
    private const val localDB: String = "jdbc:mysql://localhost:$localPort/$dbName"

    /**
     * Creating the SSH session then port-forwarding to a local port
     */
    private fun createSSHSession(): Session {
        var sshSession: Session? = null
        sshSession = JSch().getSession(sshUsername, sshHost, sshPort)
        sshSession.setPassword(sshPassword)
        sshSession.setConfig("StrictHostKeyChecking", "no")
        sshSession.connect()
        sshSession.setPortForwardingL(localPort, dbHost, dbPort)

        return sshSession
    }

    @JvmStatic
    fun connection() {

        val session: Session = createSSHSession()

        Class.forName("com.mysql.jdbc.Driver")

        try {
            val c = DriverManager.getConnection(
                localDB,
                dbUsername,
                dbPassword
            )

            println("Connection created")

            val s = c.createStatement()
//            val query = s.executeUpdate("DROP TABLE IF EXISTS testTable")
//            val query1 = s.executeUpdate("CREATE TABLE IF NOT EXISTS testTable " +
//                    "(id INTEGER not NULL," +
//                    "first VARCHAR(50)," +
//                    "PRIMARY KEY (id))")
            println("Complete!")
        }
        catch (e: SQLException) {
            e.printStackTrace()
        }

        session.disconnect()
    }

}