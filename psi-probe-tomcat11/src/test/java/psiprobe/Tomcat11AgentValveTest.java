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
package psiprobe;

import jakarta.servlet.ServletException;

import java.io.IOException;

import mockit.Mocked;
import mockit.Tested;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;

/**
 * The Class Tomcat11AgentValveTest.
 */
class Tomcat11AgentValveTest {

  /** The valve. */
  @Tested
  Tomcat11AgentValve valve;

  /** The request. */
  @Mocked
  Request request;

  /** The response. */
  @Mocked
  Response response;

  /** The valve mock. */
  @Mocked
  Valve valveMock;

  /**
   * Invoke.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ServletException the servlet exception
   */
  @Test
  void invoke() throws IOException, ServletException {
    Assertions.assertNotNull(valve);
    valve.setNext(valveMock);
    valve.invoke(request, response);
  }

}
