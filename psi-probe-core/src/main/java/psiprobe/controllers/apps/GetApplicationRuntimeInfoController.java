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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class GetApplicationRuntimeInfoController.
 */
@Controller
public class GetApplicationRuntimeInfoController extends BaseViewXmlConfController {

  @GetMapping(path = "/appruntimeinfo.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Value("ajax/appRuntimeInfo")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

  @Override
  public void setDisplayTarget(String downloadTarget) {
    super.setDisplayTarget(Long.parseLong(downloadTarget));
  }

  @Override
  public void setDownloadTarget(String downloadTarget) {
  logger.debug("setDownloadTarget");
  }

  @Value("true")
  @Override
  public void setExtendedInfo(boolean extendedInfo) {
    super.setExtendedInfo(extendedInfo);
  }

}
