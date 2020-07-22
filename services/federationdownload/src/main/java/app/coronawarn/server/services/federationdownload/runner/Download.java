/*-
 * ---license-start
 * Corona-Warn-App
 * ---
 * Copyright (C) 2020 SAP SE and all other contributors
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ---license-end
 */

package app.coronawarn.server.services.federationdownload.runner;

import app.coronawarn.server.common.persistence.domain.DiagnosisKey;
import app.coronawarn.server.common.persistence.domain.FederationBatchDownload;
import app.coronawarn.server.common.persistence.service.FederationBatchDownloadService;
import app.coronawarn.server.services.federationdownload.Application;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This runner retrieves diagnosis key batches.
 */
@Component
@Order(1)
public class Download implements ApplicationRunner {

  private static final Logger logger = LoggerFactory
      .getLogger(Download.class);

  private final ApplicationContext applicationContext;
  private final FederationBatchDownloadService federationBatchDownloadService;
  private final RestTemplate restTemplate;
  private final String url = "https://postman-echo.com/get?foo1=bar1&foo2=bar2";

  /**
   * Creates an Download, using {@link ApplicationContext}.
   */
  Download(ApplicationContext applicationContext, FederationBatchDownloadService federationBatchDownloadService,
           RestTemplate restTemplate) {
    this.applicationContext = applicationContext;
    this.federationBatchDownloadService = federationBatchDownloadService;
    this.restTemplate = restTemplate;
  }

  @Override
  public void run(ApplicationArguments args) {
    try {
      List<FederationBatchDownload> federationBatchDownloads =
          federationBatchDownloadService.getFederationBatchDownloads();



    } catch (Exception e) {
      logger.error("Download of diagnosis key batch failed.", e);
      Application.killApplication(applicationContext);
    }
    logger.debug("Batch successfully downloaded.");
  }

}