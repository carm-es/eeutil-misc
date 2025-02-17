/*
 * Copyright (C) 2012-13 MINHAP, Gobierno de España This program is licensed and may be used,
 * modified and redistributed under the terms of the European Public License (EUPL), either version
 * 1.1 or (at your option) any later version as soon as they are approved by the European
 * Commission. Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * more details. You should have received a copy of the EUPL1.1 license along with this program; if
 * not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */

package es.mpt.dsic.eeutil.misc.background;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import es.mpt.dsic.inside.utils.file.FileUtil;

public class CleanTempJob extends QuartzJobBean {

  protected static final Log logger = LogFactory.getLog(CleanTempJob.class);

  @Override
  protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    logger.warn("Inicio proceso limpiar ficheros temporales");
    FileUtil.deleteDirRecursively(new File(FileUtil.getTmpDir()));
  }

}
