// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.hcatalog.creator;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.talend.core.hadoop.conf.EHadoopConfProperties;
import org.talend.core.hadoop.conf.EHadoopConfs;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.repository.hadoopcluster.creator.AbstractHadoopSubConnectionCreator;
import org.talend.repository.model.hadoopcluster.HadoopSubConnectionItem;
import org.talend.repository.model.hcatalog.HCatalogConnection;
import org.talend.repository.model.hcatalog.HCatalogFactory;

/**
 * 
 * created by ycbai on 2015年7月1日 Detailled comment
 *
 */
public class HCatalogConnectionCreator extends AbstractHadoopSubConnectionCreator {

    @Override
    public ConnectionItem create(String relativeHadoopClusterId, Map<String, Map<String, String>> initParams)
            throws CoreException {
        HCatalogConnection connection = HCatalogFactory.eINSTANCE.createHCatalogConnection();
        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        setPropertyParameters(relativeHadoopClusterId, connectionProperty);

        HadoopSubConnectionItem connectionItem = HCatalogFactory.eINSTANCE.createHCatalogConnectionItem();
        connectionItem.setProperty(connectionProperty);
        connectionItem.setConnection(connection);

        setParameters(connection, initParams);
        appendToHadoopCluster(relativeHadoopClusterId, connectionItem);

        return connectionItem;
    }

    @Override
    public String getTypeName() {
        return EHadoopConfs.WEBHCAT.getName();
    }

    protected void setParameters(HCatalogConnection connection, Map<String, Map<String, String>> initParams) {
        if (connection == null || initParams.size() == 0) {
            return;
        }
        Map<String, String> params = initParams.get(getTypeName());
        if (params != null) {
            String templetonPort = params.get(EHadoopConfProperties.TEMPLETON_PORT.getName());
            if (StringUtils.isNotEmpty(templetonPort)) {
                connection.setPort(templetonPort);
            }
            String principal = params.get(EHadoopConfProperties.TEMPLETON_KERBEROS_PRINCIPAL.getName());
            if (StringUtils.isNotEmpty(principal)) {
                connection.setKrbPrincipal(principal);
            }
        }
    }

}
