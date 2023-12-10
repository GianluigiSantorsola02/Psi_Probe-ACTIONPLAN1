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
package psiprobe.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import psiprobe.tools.url.UrlParser;

/**
 * The Class Whois.
 */
public final class Whois {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(Whois.class);

  /**
   * Prevent Instantiation.
   */
  private Whois() {
    // Prevent Instantiation
  }

  /**
   * Lookup.
   *
   * @param server the server
   * @param port the port
   * @param query the query
   *
   * @return the response
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Response lookup(String server, int port, String query) throws IOException {
    return lookup(server, port, query, 5);
  }

  /**
   * Lookup.
   *
   * @param server the server
   * @param port the port
   * @param query the query
   * @param timeout the timeout
   *
   * @return the response
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Response lookup(String server, int port, String query, long timeout)
      throws IOException {

    return lookup(server, port, query, timeout, System.lineSeparator());
  }

  /**
   * Lookup.
   *
   * @param server the server
   * @param port the port
   * @param query the query
   * @param timeout the timeout
   * @param lineSeparator the line separator
   *
   * @return the response
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Response lookup(String server, int port, String query, long timeout, String lineSeparator) throws IOException {
    if (query == null) {
      return null;
    }

    Response response = new Response();
    response.server = server;
    response.port = port;

    try (Socket connection = AsyncSocketFactory.createSocket(server, port, timeout);
         PrintStream out = new PrintStream(connection.getOutputStream(), true, StandardCharsets.UTF_8.name());
         BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

      sendQuery(out, query);
      readAndProcessResponse(in, response, lineSeparator);

      String referral = response.getData().get("ReferralServer");
      if (referral != null) {
        handleReferral(referral, query, timeout, lineSeparator);
      }

    } catch (IOException e) {
      logger.trace("Error during lookup for '{}:{}'", server, port, e);
    }

    return response;
  }

  private static void sendQuery(PrintStream out, String query) {
    out.println(query);
  }

  private static void readAndProcessResponse(BufferedReader in, Response response, String lineSeparator) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) {
      processResponseLine(line, response);
      sb.append(line).append(lineSeparator);
    }
    response.summary = sb.toString();
  }

  private static void processResponseLine(String line, Response response) {
    line = line.trim();
    if (!line.startsWith("%") && !line.startsWith("#")) {
      int fs = line.indexOf(':');
      if (fs > 0) {
        String name = line.substring(0, fs);
        String value = line.substring(fs + 1).trim();
        response.data.put(name, value);
      }
    }
  }

  private static void handleReferral(String referral, String query, long timeout, String lineSeparator) {
    try {
      UrlParser url = new UrlParser(referral);
      if ("whois".equals(url.getProtocol())) {
        lookup(url.getHost(), url.getPort() == -1 ? 43 : url.getPort(), query, timeout, lineSeparator);
      }
    } catch (IOException e) {
      logger.trace("Could not contact '{}'", referral, e);
    }
  }

  /**
   * The Class Response.
   */
  public static final class Response {

    /** The summary. */
    String summary;

    /** The data. */
    Map<String, String> data = new TreeMap<>();

    /** The server. */
    String server;

    /** The port. */
    int port;

    /**
     * Gets the summary.
     *
     * @return the summary
     */
    public String getSummary() {
      return summary;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public Map<String, String> getData() {
      return data;
    }

    /**
     * Gets the server.
     *
     * @return the server
     */
    public String getServer() {
      return server;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
      return port;
    }

    /**
     * Instantiates a new response.
     */
    private Response() {}

  }

}
