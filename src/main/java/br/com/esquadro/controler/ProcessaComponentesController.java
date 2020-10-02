/**
 * 
 */
package br.com.esquadro.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.esquadro.model.BancoDados;
import br.com.esquadro.util.DatabaseUtils;
import br.com.esquadro.util.Statics;
import br.com.esquadro.util.Utils;
import br.com.esquadro.view.ConsoleLog;

/**
 * @author adamis.rocha
 *
 */
public class ProcessaComponentesController implements Runnable {

	private List<String> listComponent;
	private String urlProject;
	private Boolean spec;
	private ConsoleLog consoleLog;
	private Boolean mockJson;
	private BancoDados bancoDados;
	private String dbFile = "";

	public ProcessaComponentesController(List<String> listComponent, String urlProject, Boolean spec,
			ConsoleLog consoleLog, Boolean mockJson, BancoDados bancoDados) {
		this.listComponent = listComponent;
		this.urlProject = urlProject;
		this.spec = spec;
		this.consoleLog = consoleLog;
		this.mockJson = mockJson;
		this.bancoDados = bancoDados;
	}

	@Override
	public void run() {
		try {
			consoleLog.setVisible(true);
			consoleLog.moveToFront();
			consoleLog.setText("Iniciando Processamento de componentes ...");

			// CRIANDO MODULOS E COMPONENTES

			new File(this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/").mkdirs();

			dbFile = "{";

			for (int i = 0; i < listComponent.size(); i++) {

				if (listComponent.get(i) != null && !listComponent.get(i).trim().equals("")) {

					String table = listComponent.get(i);

					consoleLog.setText("Processando: " + table + "...");
					execBat(table);
					generateComponent(table);
					generateServices(table);
					generateFiltro(table);
					generateModel(table);
					updateRouting(table);
					updateModule(table);
					updateAppRoutes(table);
					updateNavBar(table);

					if (mockJson) {
						consoleLog.setText("Criando Mock...");
						if (i > 0) {
							dbFile += "\n,\n" + generateMock(table);
						} else {
							dbFile += "\n" + generateMock(table);
						}
						consoleLog.setText("Criando Mock...OK!");
					}
					consoleLog.setText("Processando...OK!");
				}

				dbFile += "}";
				// ALTERANDO O APP.MODULO.TS
				Utils.writeTxt(this.urlProject + "/data/db.json", dbFile, true);

			}

		} catch (Exception e) {

			for (int j = 0; j < e.getStackTrace().length; j++) {
				consoleLog.setText("ERRO: " + e.getStackTrace()[j]);
			}
			e.printStackTrace();
		}

		Object[] options = { "OK" };

		JOptionPane.showOptionDialog(null, "Componentes Gerados!", "Sucesso", JOptionPane.OK_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

	}

	private void generateFiltro(String table) throws Exception {

		String tableLow = Utils.normalizerString(table.trim());
		String tableCap = Utils.normalizerStringCaps(table.trim());

		String url = this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow
				+ "-pesquisa/" + tableLow + "-filtro.ts";

		String ts = "import { HttpParams } from '@angular/common/http';\r\n" + "\r\n" + "export class " + tableCap
				+ "Filtro {\r\n" + "    pagina = 0;\r\n" + "    itensPorPagina = 5;\r\n" + "    totalRegistros = 0;\r\n"
				+ "    params = new HttpParams();\r\n" + "}";
		Utils.writeTxt(url, ts, true);
	}

	/**
	 * Criar componentes com CLI
	 * 
	 * @param table
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void execBat(String table) throws IOException, InterruptedException {

		table = Utils.normalizerString(table.trim());


		String params = "";

		if (!this.spec) {
			params = " --skipTests=true";
		} else {
			params = " --skipTests=false";
		}

		String bat = urlProject.substring(0, 2) + " & " + " cd " + urlProject + " & ng g m " + Statics.MODULE_NAME + "/"
				+ table + " --routing=true" + " & ng g c " + Statics.MODULE_NAME + "/" + table + "/" + table
				+ "-cadastro" + params + " & ng g c " + Statics.MODULE_NAME + "/" + table + "/" + table + "-pesquisa"
				+ params + " & ng g s " + Statics.MODULE_NAME + "/" + table + "/" + table + params;

		// System.err.println("bat> " + bat);

		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", bat);
		pb.redirectErrorStream(true);
		Process p = pb.start();
		int waitFor = p.waitFor();

		BufferedReader output = Utils.getOutput(p);
		BufferedReader error = Utils.getError(p);

		String ligne = "";

		consoleLog.setVisible(true);
		consoleLog.moveToFront();
		consoleLog.setText("Criando novo Modulo/Componente: " + table);

		while ((ligne = output.readLine()) != null) {
			consoleLog.setText(ligne);
		}

		while ((ligne = error.readLine()) != null) {
			consoleLog.setText(ligne);
		}

		if (waitFor != 0) {
			// .service.ts
			// -pesquisa/-pesquisa.component.ts

		} else {
			System.err.println("FIM");
		}

	}

	/**
	 * Mock json
	 * 
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private String generateMock(String table) throws Exception {

		DatabaseUtils databaseUtils = new DatabaseUtils(this.bancoDados);
		String rowsJson = databaseUtils.getRowsJson(table);

		return "\"" + Utils.normalizerString(table.trim()) + "\":" + rowsJson + "";

	}

	/**
	 * Preeche componentes com conteudo
	 * 
	 * @param table
	 * @throws IOException
	 */
	private void generateComponent(String table) throws Exception {

		String tableLow = Utils.normalizerString(table.trim());
		String tableLowCammom = Utils.normalizerStringCommomNotCap(table.trim());
		String tableCap = Utils.normalizerStringCaps(table.trim());

		DatabaseUtils databaseUtils = new DatabaseUtils(this.bancoDados);
		List<HashMap<String, String>> coluns = databaseUtils.getColuns(table);
		List<HashMap<String, String>> fks = databaseUtils.getFks(table);

		// HTML-pesquisa
		String html = "<!-- CTRL + SHIFT + F for pretty -->\n" + "<app-bread-crumb [items]=\"[{label: '" + tableCap
				+ "'}]\"></app-bread-crumb>" + "\n" + " " + "\n" + "<div class=\"margin\">" + "\n" + "\n"
				+ "<p-toast></p-toast>" + "\n" + "\n" + "  <div class=\"p-col-12 p-md-12 p-lg-12\">" + "\n"
				+ "    <app-page-header page-title=\"" + tableCap
				+ "\" button-text=\"+\" button-link=\"new\" button-class=\"btn-success\"></app-page-header>" + "\n"
				+ "  </div>" + "\n" + " " + "\n" + "  <div class=\"p-grid ui-fluid\">" + "\n" + " " + "\n"
				+ "    <div class=\"p-col-12 p-md-12 p-lg-12\">" + "\n" + " " + "\n"
				+ "      <p-table #tabela [loading]=\"loading\" [responsive]=\"true\" [value]=\"resources\" [paginator]=\"true\""
				+ "\n"
				+ "        [rows]=\"filtro.itensPorPagina\" [lazy]=\"true\" [totalRecords]=\"filtro.totalRegistros\""
				+ "\n" + "        (onLazyLoad)=\"aoMudarPagina($event)\" [autoLayout]=\"true\">" + "\n" + " " + "\n"
				+ "        <ng-template pTemplate=\"header\">" + "\n" + " " + "\n" + "          <tr>" + "\n" + " ";

		for (int i = 0; i < coluns.size(); i++) {
			html += "\n" + "            <th class=\"blue-bg\">" + coluns.get(i).get("colum").toLowerCase() + "</th>";
		}

		html += "\n" + "            <th rowspan=\"2\" style=\"text-align: center;\" class=\"blue-bg\">Ações</th>" + "\n"
				+ " " + "\n" + "          </tr>" + "\n" + " " + "\n" + "          <tr>" + "\n" + " ";

		for (int i = 0; i < coluns.size(); i++) {
			html += "\n" + "            <th class=\"ui-fluid\">" + "\n"
					+ "                <input pInputText type=\"text\" name=\""
					+ coluns.get(i).get("colum").toLowerCase() + "\" (input)=\"tabela.filter($event.target.value, '"
					+ coluns.get(i).get("colum").toLowerCase() + "', 'equals')\" >" + "\n" + "            </th>";
		}

		html += "\n" + " " + "\n" + "          </tr>" + "\n" + " " + "\n" + "        </ng-template>" + "\n" + " " + "\n"
				+ "        <ng-template pTemplate=\"body\" let-" + tableLowCammom + ">" + "\n" + " " + "\n"
				+ "          <tr>" + "\n" + " ";

		for (int i = 0; i < coluns.size(); i++) {
			html += "\n" + "            <td>" + "\n" + "                {{" + tableLowCammom + "."
					+ coluns.get(i).get("colum").toLowerCase() + "}}" + "\n" + "            </td>";
		}

		html += "\n" + " " + "\n" + "            <td style=\"text-align: center;\" class=\"p-col-2\">" + "\n" + " "
				+ "\n" + "              <button pButton type=\"button\" [routerLink]=\"[" + tableLowCammom
				+ ".id, 'edit']\" icon=\"pi pi-pencil\"" + "\n"
				+ "                style=\"margin-right:9px;\"></button>" + "\n" + " " + "\n"
				+ "              <button pButton type=\"button\" (click)=\"deleteResource(" + tableLowCammom
				+ ")\" icon=\"pi pi-trash\"" + "\n" + "                class=\"ui-button-danger\"></button>" + "\n"
				+ " " + "\n" + "            </td>" + "\n" + " " + "\n" + "          </tr>" + "\n" + " " + "\n"
				+ "        </ng-template>" + "\n" + " " + "\n" + "        <ng-template pTemplate=\"emptymessage\">"
				+ "\n" + " " + "\n" + "          <tr>" + "\n" + " " + "\n" + "            <td colspan=\"6\">" + "\n"
				+ "              Nenhum registro cadastrado!" + "\n" + "            </td>" + "\n" + " " + "\n"
				+ "          </tr>" + "\n" + " " + "\n" + "        </ng-template>" + "\n" + " " + "\n"
				+ "      </p-table>" + "\n" + " " + "\n" + "    </div>" + "\n" + " " + "\n" + "  </div>" + "\n" + " "
				+ "\n" + "</div>" + "\n"
				+ "<app-base-resource-confirmation header=\"Atenção\" menssage=\"Deseja realmente deletar este item ?\"></app-base-resource-confirmation>";

		String url = this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow
				+ "-pesquisa/" + tableLow + "-pesquisa.component.html";

		Utils.writeTxt(url, html, true);

		// TS-pesquisa
		String ts = "";

		ts += "import { ConfirmationService, MessageService } from 'primeng/api';" + "\n"
				+ "import { LazyLoadEvent } from 'primeng/api';" + "\n" + "import { " + tableCap
				+ "Service } from './../" + tableLow + ".service';" + "\n" + "import { " + tableCap
				+ "} from './../../../shared/models/" + tableLow + "';" + "\n"
				+ "import { Component, ViewChild } from '@angular/core';" + "\n"
				+ "import { BaseResourceListComponent } from '../../../shared/components/base-resource-list/base-resource-list.component';"
				+ "\n" + "import { " + tableCap + "Filtro } from './" + tableLow + "-filtro';" + "\n"
				+ "import { HttpParams } from '@angular/common/http';"

				+ "\n" + "\n"

				+ "@Component({" + "\n" + "  selector: 'app-" + tableLow + "-pesquisa'," + "\n" + "  templateUrl: './"
				+ tableLow + "-pesquisa.component.html'," + "\n" + "  styleUrls: ['./" + tableLow
				+ "-pesquisa.component.css']" + "\n" + "})" + "\n" + "export class " + tableCap
				+ "PesquisaComponent extends BaseResourceListComponent<" + tableCap + "> {" + "\n" + "  filtro = new "
				+ tableCap + "Filtro();" + "\n" + "  resources = [];" + "\n" + "  loading = true;\n"
				+ "  constructor(\n" + "		private " + tableLowCammom + "Service: " + tableCap + "Service,\n"
				+ "		public confirmationService: ConfirmationService,\n"
				+ "		public messageService: MessageService\n" + "	 ) {" + "\n" + "    super(" + tableLowCammom
				+ "Service, confirmationService, messageService);" + "\n" + "  }" + "\n" + "  pesquisar(pagina = 0) {"
				+ "\n" + "    this.filtro.pagina = pagina;" + "\n" + "    this." + tableLowCammom
				+ "Service.pesquisar(this.filtro)" + "\n" + "      .then(resultado => {" + "\n"
				+ "        this.loading = false;\n" + "        this.filtro.totalRegistros = resultado.total;" + "\n"
				+ "        this.resources = resultado." + tableLowCammom + ";" + "\n" + "      })" + "\n"
				+ "		 .catch(erro => {\n" + "		        	erro = 'Erro';\n"
				+ "			    	this.loading = false;\n" + "		 		}\n" + "		 );\n" + "  }" + "\n"
				+ "  aoMudarPagina(event: LazyLoadEvent) {" + "\n" + "    const pagina = event.first / event.rows;"
				+ "\n" + "    this.filtro.params = new HttpParams();";

		for (int i = 0; i < coluns.size(); i++) {
			ts += "\n" + "    if (event.filters." + coluns.get(i).get("colum").toLowerCase() + ") {" + "\n"
					+ "      this.filtro.params = this.filtro.params.append('"
					+ coluns.get(i).get("colum").toLowerCase() + "', event.filters."
					+ coluns.get(i).get("colum").toLowerCase() + ".value);" + "\n" + "    }";
		}

		ts += "\n" + "    this.pesquisar(pagina);" + "\n" + "  }";

		ts += "\n" + "deleteResource(resource: " + tableCap + ") {" + "\n" + "    this.confirmationService.confirm({"
				+ "\n" + "      accept: () => {" + "\n"
				+ "        this.delete(resource, this.deleteSucess, this.deleteFail);" + "\n" + "      }," + "\n"
				+ "      reject: () => {" + "\n" + "" + "\n" + "      }" + "\n" + "    });" + "\n" + "  }";

		ts += "\n" + "  deleteSucess(messageService: MessageService) {" + "\n" + "    console.log('deletado');" + "\n"
				+ "    messageService.add({ severity: 'success', summary: 'Successo', detail: 'Deletado Com Sucesso!' });"
				+ "\n" + "    this.pesquisar(0);" + "\n" + "  }" + "\n" + "" + "\n"
				+ "  deleteFail(error: any, messageService: MessageService) {" + "\n" + "    console.log('error');"
				+ "\n" + "    console.log(error.error[0].mensagemUsuario);" + "\n"
				+ "    messageService.add({ severity: 'error', summary: 'Erro', detail: error.error[0].mensagemUsuario });"
				+ "\n" + "    this.pesquisar(0);" + "\n" + "  }" + "\n" + "}";

		url = this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow + "-pesquisa/"
				+ tableLow + "-pesquisa.component.ts";
		Utils.writeTxt(url, ts, true);

		// HTML-cadastro
		html = "<!-- CTRL + SHIFT + F for pretty -->" + "<app-bread-crumb [items]=\"[{label: '" + tableCap
				+ "', routerLink:'/" + tableLow + "'}, {label: pageTitle}]\"></app-bread-crumb>" + "\n" + " " + "\n"
				+ "<div class=\"margin\">" + "\n" + " " + "\n" + "  <p-panel header=\"Preencha o Formulario:\">" + "\n"
				+ " " + "\n" + "    <form [formGroup]=\"resourceForm\" (submit)=\"submitForm()\" >" + "\n" + " " + "\n"
				+ "      <div class=\"p-grid ui-fluid\" style=\"margin-top: 5px;\">" + "\n" + " ";

		for (int i = 0; i < coluns.size(); i++) {

			String type = coluns.get(i).get("type");
			String colum = Utils.normalizerStringCommomNotCap(coluns.get(i).get("colum"));

			// String columTracinho =
			// Utils.normalizerStringCommomNotCap(coluns.get(i).get("colum"));
			String columCammonNotCapInit = Utils.normalizerStringCommomNotCap(coluns.get(i).get("colum"));
			String columCap = (Character.toUpperCase(colum.charAt(0)) + colum.substring(1)).trim().replace("-", "");


			boolean controlFk = false;
			for (int j = 0; j < fks.size(); j++) {
				if (colum.equals(fks.get(j).get("column").toLowerCase())) {
					controlFk = true;
				}
			}

			if (controlFk) {
				html += "\n" + " <div class=\"p-col-12 p-md-6 p-lg-6\" formGroupName=\"" + columCammonNotCapInit
						+ "\"> " + "\n" + " 	<label for=\"" + columCammonNotCapInit + "\" >"
						+ Utils.normalizerStringCaps(colum) + "</label> " + "\n" + " 	<p-dropdown id=\""
						+ columCammonNotCapInit + "\" name=\"" + columCammonNotCapInit + "\" [options]=\""
						+ columCammonNotCapInit + "List\" placeholder=\"Selecione\" " + "\n"
						+ " 	 [filter]=\"true\" formControlName=\"id\"></p-dropdown> " + "\n" + " </div> ";
			} else if (type.toLowerCase().contains("number") || type.toLowerCase().contains("int")) {
				html += "\n" + "			<div class=\"p-col-12 p-md-2 p-lg-2\">" + "\n"
						+ "	  			<label for=\"" + colum + "\" >" + Utils.normalizerStringCaps(colum) + "</label>"
						+ "\n" + "	  			<input id=\"" + colum
						+ "\" type=\"number\" size=\"30\" pInputText formControlName=\"" + colum + "\">" + "\n"
						+ "  			<app-form-field-error [form-control]=\"resourceForm.get('" + colum
						+ "')\" header-colum=\"" + columCap + "\"></app-form-field-error>" + "\n" + "			</div>";
			} else if (type.toLowerCase().contains("timestamp")) {
				html += "\n" + "			<div class=\"p-col-12 p-md-2 p-lg-2\">" + "\n"
						+ "	  			<label for=\"" + colum + "\" >" + Utils.normalizerStringCaps(colum) + "</label>"
						+ "\n" + "	  			<p-calendar id=\"" + colum
						+ "\" [showIcon]=\"true\" dateFormat=\"dd/mm/yy\" [showTime]=\"true\" showButtonBar=\"true\" formControlName=\""
						+ colum + "\"></p-calendar>" + "\n"
						+ "  				<app-form-field-error [form-control]=\"resourceForm.get('" + colum
						+ "')\" header-colum=\"" + columCap + "\"></app-form-field-error>" + "\n" + "			</div>";
			} else if (type.toLowerCase().contains("date")) {
				html += "\n" + "			<div class=\"p-col-12 p-md-2 p-lg-2\">" + "\n"
						+ "	  			<label for=\"" + colum + "\" >" + Utils.normalizerStringCaps(colum) + "</label>"
						+ "\n" + "	  			<p-calendar id=\"" + colum
						+ "\" [showIcon]=\"true\" dateFormat=\"dd/mm/yy\" showButtonBar=\"true\" formControlName=\""
						+ colum + "\"></p-calendar>" + "\n"
						+ "  				<app-form-field-error [form-control]=\"resourceForm.get('" + colum
						+ "')\" header-colum=\"" + columCap + "\"></app-form-field-error>" + "\n" + "			</div>";
			} else {
				html += "\n" + "			<div class=\"p-col-12 p-md-6 p-lg-6\">" + "\n"
						+ "	  			<label for=\"" + colum + "\" >" + Utils.normalizerStringCaps(colum) + "</label>"
						+ "\n" + "	  			<input id=\"" + colum
						+ "\" type=\"text\" size=\"30\" pInputText formControlName=\"" + colum + "\">" + "\n"
						+ "  				<app-form-field-error [form-control]=\"resourceForm.get('" + colum
						+ "')\" header-colum=\"" + columCap + "\"></app-form-field-error>" + "\n" + "			</div>";
			}

		}

		html += "\n" + " " + "\n" + "      </div>" + "\n" + " " + "\n"
				+ "      <div class=\"p-grid ui-fluid\" style=\"margin-top: 5px;\">" + "\n" + " " + "\n" + " " + "\n"
				+ "          <div class=\"p-col-12 p-md-2 p-lg-2\">" + "\n"
				+ "              <button pButton type=\"button\" label=\"Voltar\" routerLink=\"/" + tableLow
				+ "\"  class=\"ui-button-danger\"></button>" + "\n" + "            </div>" + "\n" + " " + "\n"
				+ "          <div class=\"p-col-12 p-md-2 p-lg-2\">" + "\n"
				+ "            <button pButton type=\"button\" [disabled]=\"submittingForm || resourceForm.invalid\" type=\"submit\""
				+ "\n" + "            icon=\"pi pi-check\" label=\"Salvar\" class=\"ui-button-success\"></button>"
				+ "\n" + "          </div>" + "\n" + " " + "\n" + "      </div>" + "\n" + " " + "\n" + "    </form>"
				+ "\n" + "  </p-panel>" + "\n" + "</div>";

		url = this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow + "-cadastro/"
				+ tableLow + "-cadastro.component.html";
		Utils.writeTxt(url, html, true);

		// TS-cadastro
		String tsImport = "import { MessageService } from 'primeng/api';" + "\n" + "import { " + tableCap
				+ " } from './../../../shared/models/" + tableLow + "';" + "\n"
				+ "import { Component, Injector } from '@angular/core';" + "\n"
				+ "import { BaseResourceFormComponent } from '../../../shared/components/base-resource-form/base-resource-form.component';"
				+ "\n" + "import { " + tableCap + "Service } from '../" + tableLow + ".service';" + "\n"
				+ "import { Validators } from '@angular/forms';";

		for (int i = 0; i < fks.size(); i++) {
			if (!tsImport.contains(Utils.normalizerStringCaps(fks.get(i).get("tableRef")) + "Service")) {
				tsImport += "\n" + "import { " + Utils.normalizerStringCaps(fks.get(i).get("tableRef"))
						+ "Service } from './../../" + Utils.normalizerString(fks.get(i).get("tableRef")).toLowerCase()
						+ "/" + Utils.normalizerString(fks.get(i).get("tableRef")) + ".service';";
			}
		}
		ts = "\n";

		ts += "\n" + "@Component({" + "\n" + "  selector: 'app-" + tableLow + "-cadastro'," + "\n"
				+ "  templateUrl: './" + tableLow + "-cadastro.component.html'," + "\n" + "  styleUrls: ['./" + tableLow
				+ "-cadastro.component.css']" + "\n" + "})" + "\n" + "export class " + tableCap
				+ "CadastroComponent extends BaseResourceFormComponent<" + tableCap + "> {";

		ts += "\n";

		for (int i = 0; i < fks.size(); i++) {
			ts += "\n" + Utils.normalizerStringCommomNotCap(fks.get(i).get("column")) + "List = [];";
		}

		ts += "\n";

		ts += "\n" + "  constructor(" + "\n" + "    protected " + tableLowCammom + "Service: " + tableCap + "Service";

		for (int i = 0; i < fks.size(); i++) {
			if (!ts.contains(
					"protected " + Utils.normalizerStringCommomNotCap(fks.get(i).get("tableRef")) + "Service")) {
				ts += "\n" + "  , protected " + Utils.normalizerStringCommomNotCap(fks.get(i).get("tableRef"))
						+ "Service: " + Utils.normalizerStringCaps(fks.get(i).get("tableRef")) + "Service";
			}
		}

		ts += "\n" + "  , protected injector: Injector) {" + "\n" + "\n" + "    super(injector, new " + tableCap
				+ "(), " + tableLowCammom + "Service, " + tableCap + ".fromJson, new MessageService());";

		for (int i = 0; i < fks.size(); i++) {
			ts += "\n" + "	this.load" + Utils.normalizerStringCaps(fks.get(i).get("column")) + "();";
		}

		ts += "\n" + "\n" + "  }" + "\n" + " " + "\n" + "  protected buildResourceForm() {" + "\n"
				+ "    this.resourceForm = this.formBuilder.group({";

		for (int i = 0; i < coluns.size(); i++) {

			boolean control = false;
			for (int j = 0; j < fks.size(); j++) {
				if (coluns.get(i).get("colum").toLowerCase().equals(fks.get(j).get("column").toLowerCase())) {
					control = true;
				}
			}
			if (control) {// FK
				ts += "\n" + "		" + Utils.normalizerStringCommomNotCap(coluns.get(i).get("colum")) + ": " + "\n"
						+ "this.formBuilder.group({" + "\n" + "id: [null]" + "\n" + "}),";
			} else {
				ts += "\n" + "		" + Utils.normalizerStringCommomNotCap(coluns.get(i).get("colum"))
						+ ": [null, [Validators.required, Validators.minLength(5)]],";
			}
		}

		ts += "\n" + "    });" + "\n" + "  }" + "\n" + " ";

		// TODO
		boolean controlDate = false;

		for (int i = 0; i < coluns.size(); i++) {
			if (coluns.get(i).get("type").contains("datetime") || coluns.get(i).get("type").contains("date")) {
				controlDate = true;
			}
		}

		if (controlDate) {

			tsImport += "\n" + "import { switchMap } from 'rxjs/operators';";

			ts += "\n" + "	  //OVERRIDE" + "\n" + "    protected loadResource() {" + "\n"
					+ "        if (this.currentAction === 'edit') {" + "\n" + "            this.route.paramMap.pipe("
					+ "\n" + "                switchMap(params => this.resourceService.getById(+params.get('id')))"
					+ "\n" + "            )" + "\n" + "                .subscribe(" + "\n"
					+ "                    (resource) => {" + "\n" + " " + "\n"
					+ "                        this.resource = resource;";

			for (int i = 0; i < coluns.size(); i++) {
				if (coluns.get(i).get("type").contains("datetime") || coluns.get(i).get("type").contains("date")) {
					ts += "\n" + "                        this.resource."
							+ Utils.normalizerStringCommomNotCap(coluns.get(i).get("colum")) + " = new Date(resource."
							+ Utils.normalizerStringCommomNotCap(coluns.get(i).get("colum")) + ");";
				}
			}

			ts += "\n" + "                        this.resourceForm.patchValue(resource);" + "\n"
					+ "                    }," + "\n"
					+ "                    (error) => alert('Ocorreu um erro no servidor, tente mais tarde.')" + "\n"
					+ "                )" + "\n" + "            ;" + "\n" + "        }" + "\n" + "    }";

		}

		ts += "\n" + "  protected creationPageTitle(): string {" + "\n" + "    return 'Cadastro de Novo " + tableCap
				+ "';" + "\n" + "  }" + "\n" + " " + "\n" + "  protected editionPageTitle(): string {" + "\n"
				+ "    const " + tableLowCammom + "Name = this.resource." + coluns.get(0).get("colum").toLowerCase()
				+ " || '';" + "\n" + "    return 'Editando " + tableCap + ": ' + " + tableLowCammom + "Name;" + "\n"
				+ "  }" + "\n";

		for (int i = 0; i < fks.size(); i++) {
			// String columnNormCaps =
			// Utils.normalizerStringCaps(fks.get(i).get("tableRef"));
			String columnNormNonCaps = Utils.normalizerStringCommomNotCap(fks.get(i).get("tableRef"));

			ts += "\n" + " load" + Utils.normalizerStringCaps(fks.get(i).get("column")) + "() { " + "\n" + "     this."
					+ columnNormNonCaps + "Service.listAll() " + "\n" + "       .then(" + columnNormNonCaps + " => { "
					+ "\n" + "         this." + Utils.normalizerStringCommomNotCap(fks.get(i).get("column")) + "List = "
					+ columnNormNonCaps + ".map(c => " + "\n" + "                 ({ label: c.nome, value: c.id }) "
					+ "\n" + "           ); " + "\n" + "       }) " + "\n" + "       .catch(erro => erro); " + "\n"
					+ "   } ";
		}

		ts += "\n" + "}";

		url = this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow + "-cadastro/"
				+ tableLow + "-cadastro.component.ts";

		Utils.writeTxt(url, tsImport + ts, true);
	}

	/**
	 * Preeche Services com conteudo
	 * 
	 * @param table
	 * @throws IOException
	 */
	private void generateServices(String table) throws IOException {

		String tableLow = Utils.normalizerString(table.trim());
		String tableLowCommom = Utils.normalizerStringCommomNotCap(table.trim());
		// String tableLowCommomtoCap =
		// Utils.normalizerStringCommomNotCap(table.trim());
		String tableCap = Utils.normalizerStringCaps(table.trim());

		String ts = " import { Injectable, Injector } from '@angular/core';" + "\n" + " import { " + tableCap
				+ " } from './../../shared/models/" + tableLow + "';" + "\n"
				+ " import { BaseResourceService } from './../../shared/services/base-resource.service';" + "\n"
				+ " import { " + tableCap + "Filtro } from './" + tableLow + "-pesquisa/" + tableLow + "-filtro';"
				+ "\n" + " import { environment } from 'src/environments/environment';" + "\n" + "\n" + " @Injectable({"
				+ "\n" + "   providedIn: 'root'" + "\n" + " })" + "\n" + " export class " + tableCap
				+ "Service extends BaseResourceService<" + tableCap + "> {" + "\n" + "\n"
				+ " 	constructor(protected injector: Injector) {" + "\n" + " 	 super(environment.apiUrl + '"
				+ tableLowCommom + "', injector, " + tableCap + ".fromJson);" + "\n" + " 	}" + "\n" + "\n"
				+ " 	pesquisar(filtro: " + tableCap + "Filtro): Promise<any> {" + "\n"
				+ "  	let params = filtro.params;" + "\n" + "   	params = params" + "\n"
				+ "                .append('page', filtro.pagina.toString())" + "\n"
				+ "                .append('size', filtro.itensPorPagina.toString());" + "\n"
				+ "    	return this.http.get<any>(" + "\n" + "	    	this.apiPath," + "\n" + "	        {params}"
				+ "\n" + "      )" + "\n" + "      .toPromise()" + "\n" + "      .then(response => {" + "\n"
				+ "        const " + tableLowCommom + " = response.content;" + "\n" + "        const resultado = {"
				+ "\n" + "          " + tableLowCommom + "," + "\n" + "          total: response.totalElements" + "\n"
				+ "        };" + "\n" + "        return resultado;" + "\n" + "      });" + "\n" + "	}" + "\n" + "\n"
				+ "	listAll(): Promise<any> { " + "\n" + "     return this.http.get<any>( this.apiPath + '/' ) " + "\n"
				+ "       .toPromise() " + "\n" + "       .then(response => response.content); " + "\n" + "	} " + "\n"
				+ "\n" + "}" + "\n";

		String url = this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow
				+ ".service.ts";
		Utils.writeTxt(url, ts, true);
	}

	private void generateModel(String table) throws Exception {
		String tableLow = Utils.normalizerString(table.trim());
		String tableCap = Utils.normalizerStringCaps(table.trim());

		String tsImp = "import { BaseResourceModel } from './base-resource.model';" + "\n";

		String ts = "export class " + tableCap + " extends BaseResourceModel {" + "\n" + "constructor(\n";

		DatabaseUtils databaseUtils = new DatabaseUtils(this.bancoDados);
		List<HashMap<String, String>> coluns = databaseUtils.getColuns(table);

		for (HashMap<String, String> hashMap : coluns) {

			if (hashMap.get("type").toLowerCase().contains("int")
					|| hashMap.get("type").toLowerCase().contains("double")
					|| hashMap.get("type").toLowerCase().contains("long")
					|| hashMap.get("type").toLowerCase().contains("number")) {

				// FKS
				if (hashMap.get("fk").equals("")) {
					ts += "public " + hashMap.get("colum").toLowerCase() + "?: number,\n";
				} else {
					if (!tsImp.contains(Utils.normalizerStringCaps(hashMap.get("fk").trim()).replace("-", ""))) {
						tsImp += "import { " + Utils.normalizerStringCaps(hashMap.get("fk").trim()).replace("-", "")
								+ " } from './" + Utils.normalizerString(hashMap.get("fk").trim()) + "';" + "\n";
					}
					ts += "public " + Utils.normalizerStringCommomNotCap(hashMap.get("colum")).replace("-", "") + "?: "
							+ Utils.normalizerStringCaps(hashMap.get("fk").trim()).replace("-", "") + ",\n";
				}

			} else if (hashMap.get("type").toLowerCase().contains("date")
					|| hashMap.get("type").toLowerCase().contains("timestamp")) {

				ts += "public " + Utils.normalizerStringCommomNotCap(hashMap.get("colum")) + "?: Date,\n";

			} else if (hashMap.get("type").toLowerCase().contains("char")
					|| hashMap.get("type").toLowerCase().contains("clob")
					|| hashMap.get("type").toLowerCase().contains("long")) {

				ts += "public " + Utils.normalizerStringCommomNotCap(hashMap.get("colum")) + "?: string,\n";

			} else {

				ts += "public " + Utils.normalizerStringCommomNotCap(hashMap.get("colum")) + "?: any,\n";

			}

		}

		ts += ") {\n";
		ts += "super();\n";
		ts += "}\n";

		ts += "static fromJson(jsonData: any): " + tableCap + " {\n" + "    return Object.assign(new " + tableCap
				+ "(), jsonData);\n" + "}\n";
		ts += "}\n";

		String url = this.urlProject + "/src/app/shared/models/" + tableLow + ".ts";
		Utils.writeTxt(url, tsImp + "\n" + ts, true);
	}

	private void updateRouting(String table) throws IOException {

		String tableLow = Utils.normalizerString(table.trim());
		String tableCap = Utils.normalizerStringCaps(table.trim());
		String file = this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow
				+ "-routing.module.ts";



		List<String> readTxtList = Utils.readTxtList(file);
		String imp = "import { " + tableCap + "CadastroComponent } from './" + tableLow + "-cadastro/" + tableLow
				+ "-cadastro.component';\n" + "import { " + tableCap + "PesquisaComponent } from './" + tableLow
				+ "-pesquisa/" + tableLow + "-pesquisa.component';\n";
		readTxtList.set(0, imp + readTxtList.get(0));

		for (int i = 0; i < readTxtList.size(); i++) {
			if (readTxtList.get(i).contains("const routes: Routes")) {
				String temp = "const routes: Routes = [\n" + "\n{  path: '',        component: " + tableCap
						+ "PesquisaComponent }," + "\n{  path: 'new',    component: " + tableCap
						+ "CadastroComponent }," + "\n{  path: ':id/edit', component: " + tableCap
						+ "CadastroComponent }" + "\n];";
				readTxtList.set(i, temp);
			}

		}

		Utils.writeTxtList(file, readTxtList, true);
	}

	private void updateModule(String table) throws Exception {
		String tableLow = Utils.normalizerString(table.trim());
		String tableCap = Utils.normalizerStringCaps(table.trim());

		DatabaseUtils databaseUtils = new DatabaseUtils(this.bancoDados);
		List<HashMap<String, String>> fks = databaseUtils.getFks(table);

		String file = this.urlProject + "/src/app/" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow
				+ ".module.ts";

		List<String> readTxtList = Utils.readTxtList(file);
		String temp = "import { SharedModule } from './../../shared/shared.module';" + "\n"
				+ "import { MessagesModule } from 'primeng/messages';" + "\n"
				+ "import { MessageModule } from 'primeng/message';" + "\n"
				+ "import { ToastModule } from 'primeng/toast';" + "\n" + "import { IMaskModule } from 'angular-imask';"
				+ "\n" + "import { CalendarModule } from 'primeng/calendar';\n"
				+ "import { CardModule } from 'primeng/card';\n"
				+ "import { InputTextModule } from 'primeng/inputtext';\n"
				+ "import { KeyFilterModule } from 'primeng/keyfilter';" + "\n"
				+ "import { TableModule } from 'primeng/table';\n" + "import { PanelModule } from 'primeng/panel';\n";

		if (fks.size() > 0) {
			temp += "import {DropdownModule} from 'primeng/dropdown';\n";
		}

		temp += "\n";

		readTxtList.set(0, temp + "\n" + readTxtList.get(0));

		for (int i = 0; i < readTxtList.size(); i++) {

			if (readTxtList.get(i).contains("declarations:")) {
				temp = "declarations: [" + tableCap + "CadastroComponent, " + tableCap + "PesquisaComponent],";
				readTxtList.set(i, temp);
			}

			if (readTxtList.get(i).contains("imports: [")) {
				temp = "imports: [" + "" + "\n	SharedModule," + "\n   IMaskModule," + "\n   CalendarModule,"
						+ "\n   CardModule," + "\n   InputTextModule," + "\n   KeyFilterModule," + "\n   TableModule,"
						+ "\n   PanelModule," + "\n   MessagesModule," + "\n   MessageModule," + "\n   ToastModule,";

				if (fks.size() > 0) {
					temp += "\n   DropdownModule,";
				}

				readTxtList.set(i, temp);
			}

		}
		Utils.writeTxtList(file, readTxtList, true);
	}

	private void updateAppRoutes(String table) throws IOException {
		String tableLow = Utils.normalizerString(table.trim());
		String tableCap = Utils.normalizerStringCaps(table.trim());

		String file = this.urlProject + "/src/app/app-routing.module.ts";

		List<String> readTxtList = Utils.readTxtList(file);

		for (int i = 0; i < readTxtList.size(); i++) {
			if (readTxtList.get(i).contains("const routes: Routes")) {
				String temp = "const routes: Routes = [\n" + "	{path: '" + tableLow + "', "
						+ " loadChildren: () => import('./" + Statics.MODULE_NAME + "/" + tableLow + "/" + tableLow
						+ ".module" + "').then(m => m." + tableCap + "Module)},";

				readTxtList.set(i, temp);
			}
		}
		Utils.writeTxtList(file, readTxtList, true);
	}

	private void updateNavBar(String table) throws IOException {
		String tableLow = Utils.normalizerString(table.trim());
		String tableCap = Utils.normalizerStringCaps(table.trim());

		String file = this.urlProject + "/src/app/core/navbar/navbar.component.ts";

		List<String> readTxtList = Utils.readTxtList(file);

		boolean control = true;

		for (int i = 0; i < readTxtList.size(); i++) {
			if (readTxtList.get(i).contains("items: [") && control) {
				String temp = "					{label: '" + tableCap + "', routerLink: '" + tableLow
						+ "', command: (event) => {this.display = false; } },";
				readTxtList.set(i, readTxtList.get(i) + "\n" + temp);
				control = false;
			}
		}
		Utils.writeTxtList(file, readTxtList, true);
	}

}
