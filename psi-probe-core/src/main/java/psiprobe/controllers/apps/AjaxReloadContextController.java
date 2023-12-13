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
package psiprobe.controllers.apps;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import psiprobe.controllers.AbstractContextHandlerController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Reloads application context.
 */
@Controller
public class AjaxReloadContextController extends AbstractContextHandlerController {

  /** The Constant logger. */
  private static final Logger log16 = LoggerFactory.getLogger(AjaxReloadContextController.class);

  @GetMapping(path = "/app/reload.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  public ModelAndView handleContext(String contextName, Context context,
                                    HttpServletRequest request, HttpServletResponse response) {

    if (context != null && !request.getContextPath().equals(contextName)) {
      try {
        log16.info("{} requested RELOAD of {}", request.getRemoteAddr(), contextName);
        context.reload();
        // Logging action
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // get username logger
        String name = auth.getName();
        MessageSourceAccessor messageSourceAccessor = getMessageSourceAccessor();
        if (messageSourceAccessor != null) {
          messageSourceAccessor.getMessage("probe.src.log.reload", name);
        } else {

          log16.error("Error: getMessageSourceAccessor() returned null!");

        }
      } catch (Exception e) {
        log16.error("Error during ajax request to RELOAD of '{}'", contextName, e);
      }
    }
    return new ModelAndView(getViewName(), "available",
        context != null && getContainerWrapper().getTomcatContainer().getAvailable(context));
  }

  @Value("ajax/context_status")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
