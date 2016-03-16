/*
 * Copyright 2014 Szymon Bielak <bielakszym@gmail.com> and
 *     Michał Rus <https://michalrus.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cluo29.contextdatareading;


import io.github.cluo29.contextdatareading.table.Battery;

import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MysqlDataSource implements DataSource {

    private final Statement statement;

    public MysqlDataSource(String host, int port, String user, String pass, String database) throws SQLException, ClassNotFoundException {
        Class.forName(com.mysql.jdbc.Driver.class.getCanonicalName());
        final Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + pass);
        statement = connection.createStatement();
    }

    private ResultSet query(String table, UUID device, int id, long timestamp, int limit) throws SQLException {
        System.out.println("-------------RUNNING QUERY (" + table + ",id>" + id + ",lim=" + limit + ")-------------");
        return statement.executeQuery("SELECT * FROM " + table + " WHERE " +
                " _id > " + id + " AND " +
                " timestamp >= " + timestamp + " AND " +
                " device_id = '" + device + "' " +
                " ORDER BY _id ASC " +
                " LIMIT " + limit);
    }




    public Source<Battery> battery() {
        return new Source<Battery>() {
            public List<Battery> apply(UUID device, int withIdGreaterThan, long withTimestampGreaterEqualTo, int number) {
                List<Battery> rv = new ArrayList<Battery>();
                try{
                    ResultSet result = query("battery", device, withIdGreaterThan, withTimestampGreaterEqualTo, number);
                    while(result.next())
                        rv.add(new Battery(
                                result.getInt("_id"),
                                result.getLong("timestamp"),
                                UUID.fromString(result.getString("device_id")),
                                result.getInt("battery_status"),
                                result.getInt("battery_level"),
                                result.getInt("battery_scale"),
                                result.getInt("battery_voltage"),
                                result.getInt("battery_temperature"),
                                result.getInt("battery_adaptor"),
                                result.getInt("battery_health"),
                                result.getString("battery_technology")));
                } catch (SQLException e) { e.printStackTrace();}
                return rv;
            }
        };
    }

}
