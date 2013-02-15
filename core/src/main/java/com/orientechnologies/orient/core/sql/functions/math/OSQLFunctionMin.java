/*
 * Copyright 2010-2012 Luca Garulli (l.garulli--at--orientechnologies.com)
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
package com.orientechnologies.orient.core.sql.functions.math;

import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.sql.functions.OSQLFunctionAbstract;
import com.orientechnologies.orient.core.sql.model.OExpression;

/**
 * Compute the minimum value for a field. Uses the context to save the last minimum number. When different Number class are used,
 * take the class with most precision.
 * 
 * @author Luca Garulli (l.garulli--at--orientechnologies.com)
 * 
 */
public class OSQLFunctionMin extends OSQLFunctionAbstract {
  public static final String NAME = "min";

  private Comparable<Object> min;

  public OSQLFunctionMin() {
    super(NAME, 1, -1);
  }
  
  @Override
  protected Object evaluateNow(OCommandContext context, Object candidate) {
    
    if (getArguments().size() == 1) {
      //group by case
      final Comparable<Object> value = (Comparable<Object>)children.get(0).evaluate(context, candidate);

      if (min == null){
        // FIRST TIME
        min = value;
      }else if (min.compareTo(value) > 0){
        // SMALLER
        min = value;
      }

      return min;
    } else {
      //max of several elements
      Comparable<Object> min = null;
      for(OExpression  ex : children){
        final Comparable<Object> value = (Comparable<Object>) ex.evaluate(context, candidate);
        if(min == null){
          min = value;
        }else if (min.compareTo(value) > 0){
          min = value;
        }
      }
      return min;
    }
  }
  
  public String getSyntax() {
    return "Syntax error: min(<field> [,<field>*])";
  }

  @Override
  public OSQLFunctionMin copy() {
    final OSQLFunctionMin fct = new OSQLFunctionMin();
    fct.setAlias(alias);
    fct.getArguments().addAll(getArguments());
    return fct;
  }
  
}
