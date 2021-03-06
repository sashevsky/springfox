/*
 *
 *  Copyright 2015 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Function;
import springfox.documentation.service.AllowableValues;

public class ModelProperty {
  private final String name;
  private final ResolvedType type;
  private final String qualifiedType;
  private final int position;
  private final Boolean required;
  private final boolean isHidden;
  private final String description;
  private final AllowableValues allowableValues;
  private ModelRef modelRef;

  public ModelProperty(String name, ResolvedType type, String qualifiedType,
                       int position, Boolean required, boolean isHidden, String description,
                       AllowableValues allowableValues) {
    this.name = name;
    this.type = type;
    this.qualifiedType = qualifiedType;
    this.position = position;
    this.required = required;
    this.isHidden = isHidden;
    this.description = description;
    this.allowableValues = allowableValues;
  }

  public String getName() {
    return name;
  }

  public ResolvedType getType() {
    return type;
  }

  public String getQualifiedType() {
    return qualifiedType;
  }

  public int getPosition() {
    return position;
  }

  public Boolean isRequired() {
    return required;
  }

  public String getDescription() {
    return description;
  }

  public AllowableValues getAllowableValues() {
    return allowableValues;
  }

  public ModelRef getModelRef() {
    return modelRef;
  }

  public boolean isHidden() {
    return isHidden;
  }

  public ModelProperty updateModelRef(Function<? super ResolvedType, ModelRef> modelRefFactory) {
    modelRef = modelRefFactory.apply(type);
    return this;
  }
}
