/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.InsertMethodGenerator;
import org.mybatis.generator.config.GeneratedKey;

/**
 * 
 * @author Jeff Butler
 */
public class AnnotatedInsertMethodGenerator extends
    InsertMethodGenerator {

    public AnnotatedInsertMethodGenerator(boolean isSimple) {
        super(isSimple);
    }

    @Override
    public void addMapperAnnotations(Method method) {
        
    		//TODO 修改注解生成insert()
    	
        GeneratedKey gk = introspectedTable.getGeneratedKey();
        
        method.addAnnotation("@Insert({"); //$NON-NLS-1$
        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();
        
        javaIndent(insertClause, 1);
        javaIndent(valuesClause, 1);

        method.addAnnotation("\t\"<script>\",");
        
        insertClause.append("\"INSERT INTO "); //$NON-NLS-1$
        insertClause.append(escapeStringForJava(introspectedTable
                .getFullyQualifiedTableNameAtRuntime()));
        insertClause.append("\",\n");
        javaIndent(insertClause, 2);
        insertClause.append("\"<trim prefix=\\\"(\\\" suffix=\\\")\\\" suffixOverrides=\\\",\\\" >"); //$NON-NLS-1$
        insertClause.append("\",\n");
        javaIndent(insertClause, 3);        
        
//        valuesClause.append("\"VALUES ("); //$NON-NLS-1$
        valuesClause.append("\"<trim prefix=\\\"values (\\\" suffix=\\\")\\\" suffixOverrides=\\\",\\\" >");
        valuesClause.append("\",\n");
        javaIndent(valuesClause, 3);   

        List<String> valuesClauses = new ArrayList<String>();
        Iterator<IntrospectedColumn> iter = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns()).iterator();

        introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
//            insertClause.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            insertClause.append("\"<if test=\\\""+introspectedColumn.getJavaProperty()+" != null\\\">");
            insertClause.append(introspectedColumn.getActualColumnName()+",");
            insertClause.append("</if>\"");
            
            if (iter.hasNext()) {
              insertClause.append(","); //$NON-NLS-1$
            	method.addAnnotation(insertClause.toString());
  	          insertClause.setLength(0);
  	          javaIndent(insertClause, 2);
            }

            if (!iter.hasNext()) {
	            insertClause.append(",\n");
	            javaIndent(insertClause, 2);
	            insertClause.append("\"</trim>\",");
	            method.addAnnotation(insertClause.toString());
  	          insertClause.setLength(0);
            }
            
        }
        
        iter = introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
//            valuesClause.append(getParameterClause(introspectedColumn));
            valuesClause.append("\"<if test=\\\""+introspectedColumn.getJavaProperty()+" != null\\\">");
            valuesClause.append(getParameterClause(introspectedColumn)+",");
            valuesClause.append("</if>\"");

            if (iter.hasNext()) {
  	          valuesClause.append(","); //$NON-NLS-1$
            	method.addAnnotation(valuesClause.toString());
            	valuesClause.setLength(0);
  	          javaIndent(valuesClause, 2);
            }

            if (!iter.hasNext()) {
  	          valuesClause.append(",\n");
	            javaIndent(valuesClause, 2);
	            valuesClause.append("\"</trim>\",");
	            method.addAnnotation(valuesClause.toString());
	            valuesClause.setLength(0);
            }
        }
        
//        if (hasFields) {
//            insertClause.append(")\","); //$NON-NLS-1$
//            method.addAnnotation(insertClause.toString());
//
//            valuesClause.append(")\","); //$NON-NLS-1$
//            valuesClauses.add(valuesClause.toString());
//        }

        for (String clause : valuesClauses) {
            method.addAnnotation(clause);
        }
        
        method.addAnnotation("\t\"</script>\"");
        
        method.addAnnotation("})"); 

        method.addAnnotation("@Options(useGeneratedKeys = true, keyProperty = \"id\", keyColumn = \"id\")");
        if (gk != null) {
            addGeneratedKeyAnnotation(method, gk);
        }
    }
    
    @Override
    public void addExtraImports(Interface interfaze) {
        GeneratedKey gk = introspectedTable.getGeneratedKey();
        if (gk != null) {
            addGeneratedKeyImports(interfaze, gk);
        }
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Insert")); //$NON-NLS-1$
    }
}
