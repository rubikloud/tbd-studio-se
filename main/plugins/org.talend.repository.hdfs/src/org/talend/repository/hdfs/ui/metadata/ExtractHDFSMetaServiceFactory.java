// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.hdfs.ui.metadata;

import org.talend.core.utils.ReflectionUtils;
import org.talend.designer.hdfsbrowse.manager.HadoopServerUtil;
import org.talend.designer.hdfsbrowse.model.HDFSConnectionBean;
import org.talend.repository.hadoopcluster.service.IExtractSchemaService;
import org.talend.repository.model.hdfs.HDFSConnection;

/**
 * created by ycbai on 2014-5-29 Detailled comment
 * 
 */
public class ExtractHDFSMetaServiceFactory {

    /**
     * Get the appropriate implementation of {@link IExtractSchemaService} for the given {@code path} of file. This
     * method should be improved if there are more types of file to support in the future.
     * <p>
     * 
     * DOC ycbai Comment method "getService".
     * 
     * @param connectionBean
     * @param path
     * @return
     */
    public static IExtractSchemaService<HDFSConnection> getService(HDFSConnectionBean connectionBean, Object path) {
        IExtractSchemaService<HDFSConnection> service = new ExtractTextFileSchemaService();
        boolean isSequenceFile = true;
        try {
            ClassLoader classLoader = HadoopServerUtil.getClassLoader(connectionBean);
            Object fs = HadoopServerUtil.getDFS(connectionBean);
            Object conf = HadoopServerUtil.getConfiguration(connectionBean);
            ReflectionUtils.newInstance("org.apache.hadoop.io.SequenceFile$Reader", classLoader, new Object[] { fs, path, conf },
                    Class.forName("org.apache.hadoop.fs.FileSystem", true, classLoader),
                    Class.forName("org.apache.hadoop.fs.Path", true, classLoader),
                    Class.forName("org.apache.hadoop.conf.Configuration", true, classLoader));
        } catch (Exception e) {
            isSequenceFile = false;
        }
        if (isSequenceFile) {
            service = new ExtractSequenceFileSchemaService();
        }

        return service;
    }

}
