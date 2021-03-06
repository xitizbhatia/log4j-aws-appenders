// Copyright (c) Keith D Gregory
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.kdgregory.logging.aws.internal.retrievers;


/**
 *  Retrieves a value from the Systems Manager Parameter Store. Supports <code>String</code>
 *  and <code>StringList</code> values; returns <code>null</code> for <code>SecureString</code>
 *  values.
 *  <p>
 *  Note: Parameter Store was not available in the early 1.11.x SDK releases, so this uses a
 *  client builder rather than a constructor (and therefore doesn't need to worry about region).
 */
public class ParameterStoreRetriever
extends AbstractReflectionBasedRetriever
{
    private Class<?> parameterKlass;

    public ParameterStoreRetriever()
    {
        super("com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder",
              "com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement",
              "com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest",
              "com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult");
        parameterKlass = loadClass("com.amazonaws.services.simplesystemsmanagement.model.Parameter");
    }


    public String invoke(String key)
    {
        Object client = invokeBuilder();
        try
        {
            Object request = instantiate(requestKlass);
            setRequestValue(request, "setName", String.class, key);
            Object response = invokeRequest(client, "getParameter", request);
            Object parameter = getResponseValue(response, "getParameter", parameterKlass);
            Object paramType = invokeMethod(parameterKlass, parameter, "getType", null, null);
            return ("SecureString".equals(paramType))
                 ? null
                 : (String)invokeMethod(parameterKlass, parameter, "getValue", null, null);
        }
        finally
        {
            shutdown(client);
        }
    }
}
