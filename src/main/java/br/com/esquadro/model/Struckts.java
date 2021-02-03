/**
 * 
 */
package br.com.esquadro.model;

import java.util.List;

import com.google.googlejavaformat.java.Formatter;

import br.com.esquadro.util.DatabaseUtils;
import br.com.esquadro.util.IgnoreCaseStr;
import br.com.esquadro.util.Utils;
import br.com.esquadro.view.ConsoleLog;

/**
 * @author Adamis
 *
 */
public class Struckts {

	private String entity;
	private String packBase;
	private String packEntity;
	private String packResource;
	private String packFilter;
	private String packRepository;
	private String packServices;
	private String packRepositoryImpl;
	private ConsoleLog console;

	private DatabaseUtils databaseUtils;
	private StringBuilder imports = new StringBuilder();

	public Struckts(String entity, String packBase, String packEntity, String packResource, String packFilter,
			String packRepository, String packServices, String packRepositoryImpl, DatabaseUtils databaseUtils,
			ConsoleLog console) {

		this.entity = entity.trim();
		this.packBase = packBase.trim();
		this.packEntity = packEntity.trim();
		this.packResource = packResource.trim();
		this.packFilter = packFilter.trim();
		this.packRepository = packRepository.trim();
		this.packServices = packServices.trim();
		this.packRepositoryImpl = packRepositoryImpl.trim();
		this.databaseUtils = databaseUtils;
		this.console = console;
	}

	public String getResource() {

		StringBuilder sb = new StringBuilder();
		sb.append("package {{packResource}}; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import java.util.Optional; ");
		sb.append("\n");
		sb.append("import javax.servlet.http.HttpServletResponse; ");
		sb.append("\n");
		sb.append("import org.springframework.validation.annotation.Validated;");
		sb.append("\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired; ");
		sb.append("\n");
		sb.append("import org.springframework.data.domain.Page; ");
		sb.append("\n");
		sb.append("import org.springframework.data.domain.Pageable; ");
		sb.append("\n");
		sb.append("import org.springframework.http.HttpStatus; ");
		sb.append("\n");
		sb.append("import org.springframework.http.ResponseEntity; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.DeleteMapping; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.GetMapping; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.PathVariable; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.PostMapping; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.PutMapping; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.RequestBody; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.RequestMapping; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.ResponseStatus; ");
		sb.append("\n");
		sb.append("import org.springframework.web.bind.annotation.RestController; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import {{packEntity}}.{{entity}}; ");
		sb.append("\n");
		sb.append("import {{packRepository}}.{{entity}}Repository; ");
		sb.append("\n");
		sb.append("import {{packFilter}}.{{entity}}Filter; ");
		sb.append("\n");
		sb.append("import {{packServices}}.{{entity}}Service; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("@RestController ");
		sb.append("\n");
		sb.append("@RequestMapping(\"/{{entityL}}\") ");
		sb.append("\n");
		sb.append("public class {{entity}}Resource { ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append(" 	@Autowired ");
		sb.append("\n");
		sb.append(" 	private {{entity}}Repository {{entityL}}Repository; ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append(" 	@Autowired ");
		sb.append("\n");
		sb.append(" 	private {{entity}}Service {{entityL}}Service; ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append(" 	@PostMapping	 ");
		sb.append("\n");
		sb.append(
				" 	public ResponseEntity<{{entity}}> criar( @RequestBody {{entity}} {{entityL}}, HttpServletResponse response) { ");
		sb.append("\n");
		sb.append(" 		{{entity}} {{entityL}}Salva = {{entityL}}Repository.save({{entityL}}); ");
		sb.append("\n");
		sb.append(" 		return ResponseEntity.status(HttpStatus.CREATED).body({{entityL}}Salva); ");
		sb.append("\n");
		sb.append(" 	} ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append(" 	@GetMapping(\"/{codigo}\")	 ");
		sb.append("\n");
		sb.append(" 	public ResponseEntity<{{entity}}> buscarPeloCodigo(@PathVariable Long codigo) { ");
		sb.append("\n");
		sb.append(" 		Optional<{{entity}}> {{entityL}} = {{entityL}}Repository.findById(codigo); ");
		sb.append("\n");
		sb.append(
				" 		return {{entityL}} != null ? ResponseEntity.ok({{entityL}}.get()) : ResponseEntity.notFound().build(); ");
		sb.append("\n");
		sb.append(" 	} ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append(" 	@DeleteMapping(\"/{codigo}\") ");
		sb.append("\n");
		sb.append(" 	@ResponseStatus(HttpStatus.NO_CONTENT)	 ");
		sb.append("\n");
		sb.append(" 	public void remover(@PathVariable Long codigo) { ");
		sb.append("\n");
		sb.append(" 		{{entityL}}Repository.deleteById(codigo); ");
		sb.append("\n");
		sb.append(" 	} ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append(" 	@PutMapping(\"/{codigo}\")	 ");
		sb.append("\n");
		sb.append(
				" 	public ResponseEntity<{{entity}}> atualizar(@PathVariable Long codigo, @Validated @RequestBody {{entity}} {{entityL}}) { ");
		sb.append("\n");
		sb.append(" 		{{entity}} {{entityL}}Salva = {{entityL}}Service.atualizar(codigo, {{entityL}}); ");
		sb.append("\n");
		sb.append(" 		return ResponseEntity.ok({{entityL}}Salva); ");
		sb.append("\n");
		sb.append(" 	} ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append(" 	@GetMapping	 ");
		sb.append("\n");
		sb.append(" 	public Page<{{entity}}> pesquisar({{entity}}Filter {{entityL}}Filter, Pageable pageable) { ");
		sb.append("\n");
		sb.append(" 		return {{entityL}}Repository.filtrar({{entityL}}Filter, pageable); ");
		sb.append("\n");
		sb.append(" 	} ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append("} ");

		return processClean(sb);
	}

	public String getRepository() {

		StringBuilder sb = new StringBuilder();
		sb.append("package {{packRepository}}; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import org.springframework.data.jpa.repository.JpaRepository; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import {{packEntity}}.{{entity}}; ");
		sb.append("\n");
		sb.append("import {{packRepositoryImpl}}.{{entity}}RepositoryQuery; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append(
				"public interface {{entity}}Repository extends JpaRepository<{{entity}}, Long>, {{entity}}RepositoryQuery { ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("} ");

		return processClean(sb);

	}

	public String getFilter() throws Exception {

		List<TransferDTO> coluns = databaseUtils.getColuns(entity);
		StringBuilder sbImport = new StringBuilder();
		sbImport.append("package {{packFilter}}; ");
		sbImport.append("\n");
		sbImport.append(" ");
		sbImport.append("\n");
		sbImport.append("import lombok.Data; ");

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("\n");
		sb.append("@Data");
		sb.append("\n");
		sb.append("public class {{entity}}Filter { ");

		for (int i = 0; i < coluns.size(); i++) {
			sb.append("\n");

			String colum = "";
			if (!coluns.get(i).getFk().isEmpty()) {
				colum = "Filter";

				sb.append("	private "
						+ processTypeDatabase(coluns.get(i).getType(), coluns.get(i).getFk(), sbImport) + " "
						+ Utils.normalizerStringCommomNotCap(coluns.get(i).getFk())
						+ (Utils.normalizerStringCommomNotCap(coluns.get(i).getColumn()).equalsIgnoreCase("id") ? ""
								: colum)
						+ ";");

			} else {

				sb.append("	private "
						+ processTypeDatabase(coluns.get(i).getType(), coluns.get(i).getFk(), sbImport) + " "
						+ Utils.normalizerStringCommomNotCap(coluns.get(i).getColumn())
						+ (Utils.normalizerStringCommomNotCap(coluns.get(i).getColumn()).equalsIgnoreCase("id") ? ""
								: colum)
						+ ";");

			}
		}

		sb.append("\n");
		sb.append("} ");

		sbImport.append(sb.toString().replace("BigDecimal", "Long").replace("Bigdecimal", "Long"));

		return processClean(sbImport);
	}

	public String getServices() {

		StringBuilder sb = new StringBuilder();
		sb.append("package {{packServices}};");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import java.util.Optional; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import org.springframework.beans.BeanUtils; ");
		sb.append("\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired; ");
		sb.append("\n");
		sb.append("import org.springframework.dao.EmptyResultDataAccessException; ");
		sb.append("\n");
		sb.append("import org.springframework.stereotype.Service; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import {{packEntity}}.{{entity}}; ");
		sb.append("\n");
		sb.append("import {{packRepository}}.{{entity}}Repository; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("@Service ");
		sb.append("\n");
		sb.append("public class {{entity}}Service { ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append(" 	@Autowired ");
		sb.append("\n");
		sb.append(" 	private {{entity}}Repository {{entityL}}Repository; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append(" 	public {{entity}} atualizar(Long codigo, {{entity}} {{entityL}}) { ");
		sb.append("\n");
		sb.append(" 		{{entity}} {{entityL}}Salva = buscarPeloCodigo(codigo); ");
		sb.append("\n");
		sb.append(" 		 ");
		sb.append("\n");
		sb.append(" 		BeanUtils.copyProperties({{entityL}}, {{entityL}}Salva, \"id\"); ");
		sb.append("\n");
		sb.append(" 		return {{entityL}}Repository.save({{entityL}}Salva); ");
		sb.append("\n");
		sb.append(" 	}	 ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append("\n");
		sb.append(" 	public {{entity}} buscarPeloCodigo(Long codigo) { ");
		sb.append("\n");
		sb.append(" 		Optional<{{entity}}> {{entityL}}Salva = {{entityL}}Repository.findById(codigo); ");
		sb.append("\n");
		sb.append(" 		if ({{entityL}}Salva == null) { ");
		sb.append("\n");
		sb.append(" 			throw new EmptyResultDataAccessException(1); ");
		sb.append("\n");
		sb.append(" 		} ");
		sb.append("\n");
		sb.append(" 		return {{entityL}}Salva.get(); ");
		sb.append("\n");
		sb.append(" 	} ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append("} ");

		return processClean(sb);
	}

	public String getRepositoryImpl() {

		StringBuilder packageImpl = new StringBuilder();
		imports = new StringBuilder();
		StringBuilder sb = new StringBuilder();

		packageImpl.append("package {{packRepository}}.{{entityL}}; ");
		packageImpl.append("\n");

		sb.append(" ");
		sb.append("\n");

		imports.append("import java.util.ArrayList; ");
		imports.append("\n");
		imports.append("import java.util.List; ");
		imports.append("\n");
		imports.append(" ");
		imports.append("\n");
		imports.append("import javax.persistence.EntityManager; ");
		imports.append("\n");
		imports.append("import javax.persistence.PersistenceContext; ");
		imports.append("\n");
		imports.append("import javax.persistence.TypedQuery; ");
		imports.append("\n");
		imports.append("import javax.persistence.criteria.CriteriaBuilder; ");
		imports.append("\n");
		imports.append("import javax.persistence.criteria.CriteriaQuery; ");
		imports.append("\n");
		imports.append("import javax.persistence.criteria.Order; ");
		imports.append("\n");
		imports.append("import javax.persistence.criteria.Predicate; ");
		imports.append("\n");
		imports.append("import javax.persistence.criteria.Root; ");
		imports.append("\n");
		imports.append(" ");
		imports.append("\n");
		imports.append("import org.springframework.data.domain.Page; ");
		imports.append("\n");
		imports.append("import org.springframework.data.domain.PageImpl; ");
		imports.append("\n");
		imports.append("import org.springframework.data.domain.Pageable; ");
		imports.append("\n");
		imports.append("import org.springframework.data.jpa.repository.query.QueryUtils; ");
		imports.append("\n");
		imports.append("import org.springframework.util.StringUtils; ");
		imports.append("\n");
		imports.append(" ");
		imports.append("\n");
		imports.append("import {{packEntity}}.{{entity}}; ");
		imports.append("\n");
		imports.append("import {{packEntity}}.{{entity}}_; ");
		imports.append("\n");
		imports.append("import {{packFilter}}.{{entity}}Filter; ");

		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("public class {{entity}}RepositoryImpl implements {{entity}}RepositoryQuery { ");
		sb.append("\n");
		sb.append("  ");

		// FILTRAR
		sb.append("\n");
		sb.append("@PersistenceContext");
		sb.append("\n");
		sb.append("		private EntityManager manager;");
		sb.append("\n");
		sb.append("		");
		sb.append("\n");
		sb.append("		@Override");
		sb.append("\n");
		sb.append("		public Page<{{entity}}> filtrar({{entity}}Filter {{entityL}}Filter, Pageable pageable) {");
		sb.append("\n");
		sb.append("			CriteriaBuilder builder = manager.getCriteriaBuilder();");
		sb.append("\n");
		sb.append("			CriteriaQuery<{{entity}}> criteria = builder.createQuery({{entity}}.class);");
		sb.append("\n");
		sb.append("			Root<{{entity}}> root = criteria.from({{entity}}.class);");
		sb.append("\n");
		sb.append("			");
		sb.append("\n");
		sb.append("			List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);");
		sb.append("\n");
		sb.append("			");
		sb.append("\n");
		sb.append("			Predicate[] predicates = criarRestricoes({{entityL}}Filter, builder, root);");
		sb.append("\n");
		sb.append("			criteria.where(predicates)");
		sb.append("\n");
		sb.append("			.orderBy(orders)");
		sb.append("\n");
		sb.append("			;		");
		sb.append("\n");
		sb.append("			");
		sb.append("\n");
		sb.append("			");
		sb.append("\n");
		sb.append("			TypedQuery<{{entity}}> query = manager.createQuery(criteria);");
		sb.append("\n");
		sb.append("			adicionarRestricoesDePaginacao(query, pageable);");
		sb.append("\n");
		sb.append("			");
		sb.append("\n");
		sb.append("			return new PageImpl<>(query.getResultList(), pageable, total({{entityL}}Filter));");
		sb.append("\n");
		sb.append("		}");
		sb.append("\n");
		sb.append("");
		sb.append("\n");
		sb.append("	private Predicate[] criarRestricoes({{entity}}Filter {{entityL}}Filter, CriteriaBuilder builder,");
		sb.append("\n");
		sb.append("			Root<{{entity}}> root) {");
		sb.append("\n");
		sb.append("		List<Predicate> predicates = new ArrayList<>();");
		sb.append("\n");
		sb.append("		");
		sb.append("\n");

		// FOR PREDICATES
		// TODO
		sb.append(montaPredicados(this.entity, "", ""));

		sb.append("\n");
		sb.append("				");
		sb.append("\n");
		sb.append("		return predicates.toArray(new Predicate[predicates.size()]);");
		sb.append("\n");
		sb.append("	}");
		sb.append("\n");
		sb.append("");
		sb.append("\n");
		sb.append("	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {");
		sb.append("\n");
		sb.append("		int paginaAtual = pageable.getPageNumber();");
		sb.append("\n");
		sb.append("		int totalRegistrosPorPagina = pageable.getPageSize();");
		sb.append("\n");
		sb.append("		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;");
		sb.append("\n");
		sb.append("		");
		sb.append("\n");
		sb.append("		query.setFirstResult(primeiroRegistroDaPagina);");
		sb.append("\n");
		sb.append("		query.setMaxResults(totalRegistrosPorPagina);");
		sb.append("\n");
		sb.append("	}");
		sb.append("\n");
		sb.append("	");
		sb.append("\n");
		sb.append("	private Long total({{entity}}Filter filter) {");
		sb.append("\n");
		sb.append("		CriteriaBuilder builder = manager.getCriteriaBuilder();");
		sb.append("\n");
		sb.append("		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);");
		sb.append("\n");
		sb.append("		Root<{{entity}}> root = criteria.from({{entity}}.class);");
		sb.append("\n");
		sb.append("		");
		sb.append("\n");
		sb.append("		Predicate[] predicates = criarRestricoes(filter, builder, root);");
		sb.append("\n");
		sb.append("		criteria.where(predicates);");
		sb.append("\n");
		sb.append("		");
		sb.append("\n");
		sb.append("		criteria.select(builder.count(root));");
		sb.append("\n");
		sb.append("		return manager.createQuery(criteria).getSingleResult();");
		sb.append("\n");
		sb.append("	}");

		sb.append("\n");
		sb.append("	");
		sb.append("\n");
		sb.append("	");
		sb.append("\n");
		sb.append("}");

		packageImpl.append(imports).append(sb);

		return processClean(packageImpl);
	}

	public String montaPredicados(String entidade, String last, String fkCriteria) {

		StringBuilder sb = new StringBuilder();

		try {
			List<TransferDTO> coluns = this.databaseUtils.getColuns(entidade);

			for (int i = 0; i < coluns.size(); i++) {

				sb.append("\n");
				sb.append("//" + Utils.normalizerStringCapHifen(coluns.get(i).getColumn().replace("-", "_"),
						databaseUtils.getBancoDados().getTipo()).toUpperCase());

				String type = coluns.get(i).getType().toLowerCase();

				// System.err.println("COLUM: " + coluns.get(i).getColumn());

				IgnoreCaseStr ignoreCaseStr = new IgnoreCaseStr(type);
				
				if (ignoreCaseStr.contains("char")) {
					// STRING
					sb.append("\n");
					sb.append(" if (StringUtils.hasLength({{entityL}}Filter." + last + "get"
							+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "())) { ");
					sb.append("\n");
					sb.append(" 	predicates.add(builder.like( ");
					sb.append("\n");

					sb.append(" 		builder.lower(root." + fkCriteria + "get("
							+ Utils.normalizerStringCaps(entidade) + "_."
							+ Utils.normalizerStringCapHifen(coluns.get(i).getColumn().replace("-", "_"),
									databaseUtils.getBancoDados().getTipo()).toUpperCase()
							+ ")), \"%\" + {{entityL}}Filter." + last + "get"
							+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "().toLowerCase() + \"%\")); ");
					sb.append("\n");
					sb.append(" } ");
					sb.append("\n");

				} else if (
						ignoreCaseStr.contains("real") || ignoreCaseStr.contains("dec") || ignoreCaseStr.contains("num") || 
						ignoreCaseStr.contains("number")	|| ignoreCaseStr.contains("double") || ignoreCaseStr.contains("int") || 
						ignoreCaseStr.contains("long") 
				) {

					// INTEGER
					if (!coluns.get(i).getFk().isEmpty()) {

						if (!imports.toString()
								.contains(Utils.normalizerStringCaps(coluns.get(i).getFk().replace("-", "_")) + "_; ")
								&& !coluns.get(i).getFk().equals(entidade)
								&& !Utils.normalizerStringCaps(this.entity).equalsIgnoreCase(entidade)) {

							imports.append("\n");
							imports.append("import {{packEntity}}."
									+ Utils.normalizerStringCaps(coluns.get(i).getFk().replace("-", "_")) + "_; ");
							imports.append("\n");

						}

						if (!coluns.get(i).getFk().equals(entidade)) {

							// FK
							sb.append("\n");
							sb.append("	if ({{entityL}}Filter." + last + "get"
									+ Utils.normalizerStringCaps(coluns.get(i).getFk()) + "Filter() != null) { ");

							String montaPredicados = montaPredicados(coluns.get(i).getFk(),
									last + "get" + Utils
									.normalizerStringCaps(coluns.get(i).getFk()) + "Filter().",
									fkCriteria + "get(" + Utils.normalizerStringCaps(entidade) + "_."
											+ Utils.normalizerStringCapHifen(coluns.get(i).getFk(),
													databaseUtils.getBancoDados().getTipo()).toUpperCase()
											+ ").");
							sb.append(montaPredicados);

							sb.append("\n");
							sb.append("	}");
							sb.append("\n");

						} else {

							sb.append("\n");
							sb.append("/*");
							sb.append("\n");
							sb.append(" if ({{entityL}}Filter." + last + "get"
									+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "() != null) { ");
							sb.append("\n");
							sb.append(" 	predicates.add(builder.equal( ");
							sb.append("\n");
							sb.append(" 		root." + fkCriteria + "get(" + Utils.normalizerStringCaps(entidade)
							+ "_."
							+ Utils.normalizerStringCapHifen(coluns.get(i).getColumn().replace("-", "_"),
									databaseUtils.getBancoDados().getTipo()).toUpperCase()
							+ "), {{entityL}}Filter." + last + "get"
							+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "())); ");
							sb.append("\n");
							sb.append(" } ");
							sb.append("\n");
							sb.append("*/");
						}
					} else {
						sb.append("\n");
						sb.append(" if ({{entityL}}Filter." + last + "get"
								+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "() != null) { ");
						sb.append("\n");
						sb.append(" 	predicates.add(builder.equal( ");
						sb.append("\n");
						sb.append(" 		root." + fkCriteria + "get(" + Utils.normalizerStringCaps(entidade) + "_."
								+ Utils.normalizerStringCapHifen(coluns.get(i).getColumn().replace("-", "_"),
										databaseUtils.getBancoDados().getTipo()).toUpperCase()
								+ "), {{entityL}}Filter." + last + "get"
								+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "())); ");
						sb.append("\n");
						sb.append(" } ");
					}
				} else if (ignoreCaseStr.contains("date") || ignoreCaseStr.contains("time")) {
					// DATE
					sb.append("\n");
					sb.append(" if ({{entityL}}Filter." + last + "get"
							+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "() != null) { ");
					sb.append("\n");
					sb.append(" 	predicates.add(builder.equal( ");
					sb.append("\n");
					sb.append(" 		root." + fkCriteria + "get(" + Utils.normalizerStringCaps(entidade) + "_."
							+ Utils.normalizerStringCapHifen(coluns.get(i).getColumn().replace("-", "_"),
									databaseUtils.getBancoDados().getTipo()).toUpperCase()
							+ "), {{entityL}}Filter." + last + "get"
							+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "())); ");
					sb.append("\n");
					sb.append(" } ");
					sb.append("\n");
				} else {
					// OTHER
					sb.append("\n");
					sb.append(" if (StringUtils.hasLength({{entityL}}Filter." + last + "get"
							+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "())) { ");
					sb.append("\n");
					sb.append(" 	predicates.add(builder.equal( ");
					sb.append("\n");
					sb.append(" 		root." + fkCriteria + "get(" + Utils.normalizerStringCaps(entidade) + "_."
							+ Utils.normalizerStringCapHifen(coluns.get(i).getColumn().replace("-", "_"),
									databaseUtils.getBancoDados().getTipo()).toUpperCase()
							+ "), {{entityL}}Filter." + last + "get"
							+ Utils.normalizerStringCaps(coluns.get(i).getColumn()) + "())); ");
					sb.append("\n");
					sb.append(" } ");
					sb.append("\n");
				}

			}

		} catch (Exception e) {
			System.err.println("erro>> " + e.getMessage());
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String getRepositoryQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("package {{packRepositoryImpl}}; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import org.springframework.data.domain.Page; ");
		sb.append("\n");
		sb.append("import org.springframework.data.domain.Pageable; ");
		sb.append("\n");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("import {{packEntity}}.{{entity}}; ");
		sb.append("\n");
		sb.append("import {{packFilter}}.{{entity}}Filter; ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("public interface {{entity}}RepositoryQuery { ");
		sb.append("\n");
		sb.append("  ");
		sb.append("\n");
		sb.append("	public Page<{{entity}}> filtrar({{entity}}Filter {{entityL}}Filter, Pageable pageable);	 ");
		sb.append("\n");
		sb.append(" 	 ");
		sb.append("\n");
		sb.append("} ");

		return processClean(sb);
	}

	private String processClean(StringBuilder sb) {

		try {
			String replace = sb.toString().replace("{{packBase}}", packBase.replace("/", "."))
					.replace("{{packEntity}}", packEntity.replace("/", "."))
					.replace("{{packResource}}", packResource.replace("/", "."))
					.replace("{{packFilter}}", packFilter.replace("/", "."))
					.replace("{{packRepository}}", packRepository.replace("/", "."))
					.replace("{{packServices}}", packServices.replace("/", "."))
					.replace("{{packRepositoryImpl}}", packRepositoryImpl.replace("/", "."))
					.replace("{{entity}}", Utils.normalizerStringCaps(this.entity))
					.replace("{{EntityFolder}}", Utils.normalizerStringCommomNotCap(this.entity))
					.replace("{{entityL}}", Utils.normalizerStringCommomNotCap(this.entity));

			return new Formatter().formatSourceAndFixImports(replace);

		} catch (Exception e) {
			e.printStackTrace();
			this.console.setText("" + e.getMessage());
			this.console.setText("" + e.getLocalizedMessage());
			this.console.setText("" + e.getCause());
			return "";
		}

	}

	private String processTypeDatabase(String type, String fk, StringBuilder sbImport) {
		type = type.toLowerCase();
		System.err.println("type>> "+ type);
		if (fk.isEmpty()) {

			if (type.contains("char")) {
				type = "String";
			} else if (type.contains("int")) {
				type = "Integer";
			} else if (type.contains("double")) {
				type = "Double";
			} else if (type.contains("real")) {
				type = "Double";
			} else if (type.contains("date") || type.contains("time")) {
				if (!sbImport.toString().contains("java.util.Date")) {
					sbImport.append("\n");
					sbImport.append("import java.util.Date;");
				}
				type = "Date";
			} else if (type.contains("dec")) {
				if (!sbImport.toString().contains("java.math.BigDecimal")) {
					sbImport.append("\n");
					sbImport.append("import java.math.BigDecimal;");
				}
				type = "BigDecimal";
			} else if (type.contains("num")) {				
				type = "Long";
			} else if (type.contains("long")) {
				type = "Long";
			} else {
				type = "Object";
			}

		} else {
			type = Utils.normalizerStringCaps(fk) + "Filter";
		}

		return type;
	}

}
