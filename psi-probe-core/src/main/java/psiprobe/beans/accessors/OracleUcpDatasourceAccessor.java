/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans.accessors;

import psiprobe.model.DataSourceInfo;

import oracle.ucp.jdbc.JDBCConnectionPoolStatistics;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceImpl;
import oracle.ucp.jdbc.PoolXADataSourceImpl;

/**
 * Accesses an Oracle Universal Connection Pool (UCP) resource.
 */
public class OracleUcpDatasourceAccessor implements DatasourceAccessor {

  @Override
  public DataSourceInfo getInfo(Object resource) {
    DataSourceInfo dataSourceInfo = null;
    if (canMap(resource)) {
      PoolDataSource source = (PoolDataSource) resource;
      JDBCConnectionPoolStatistics stats = source.getStatistics();

      dataSourceInfo = new DataSourceInfo();
      /*
       * If the pool starts with 0 instances, the JDBCConnectionPoolStatistics will be null. The
       * JDBCConnectionPoolStatistics only initializes when there is at least one connection
       * instance created.
       */
      if (stats != null) {
        dataSourceInfo.setBusyConnections(stats.getBorrowedConnectionsCount());
        dataSourceInfo.setEstablishedConnections(stats.getTotalConnectionsCount());
      } else {
        dataSourceInfo.setBusyConnections(0);
        dataSourceInfo.setEstablishedConnections(0);
      }
      dataSourceInfo.setMaxConnections(source.getMaxPoolSize());
      dataSourceInfo.setJdbcUrl(source.getURL());
      dataSourceInfo.setUsername(source.getUser());
      dataSourceInfo.setResettable(false);
      dataSourceInfo.setType("oracle-ucp");
    }
    return dataSourceInfo;
  }

  @Override
  public boolean reset(Object resource) {
    return false;
  }

  @Override
  public boolean canMap(Object resource) {
    return resource instanceof PoolDataSourceImpl;
  }

}
