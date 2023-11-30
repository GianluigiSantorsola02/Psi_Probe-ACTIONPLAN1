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
package psiprobe.controllers.connectors;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.beans.ContainerListenerBean;
import psiprobe.controllers.AbstractTomcatContainerController;
import psiprobe.model.Connector;

import static psiprobe.beans.ContainerListenerBean.*;

/**
 * The Class BaseGetConnectorController.
 */
public class BaseGetConnectorController extends AbstractTomcatContainerController {

  /** The container listener bean. */
  @Inject
  private ContainerListenerBean containerListenerBean;

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String connectorName;
    connectorName = ServletRequestUtils.getStringParameter(request, "cn", null);
    Connector connector = null;

    List<Connector> connectors;
    try {
      connectors = containerListenerBean.getConnectors(false);
    } catch (CustomException e) {
      throw new RuntimeException(e);
    }
    for (Connector conn : connectors) {
        if (connectorName.equals(conn.getProtocolHandler())) {
          connector = conn;
          break;
        }
      }

      assert connector != null;


      return new ModelAndView(getViewName(), "connector", connector);
  }

}
