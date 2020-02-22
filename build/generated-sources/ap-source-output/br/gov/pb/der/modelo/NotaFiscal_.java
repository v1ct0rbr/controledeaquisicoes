package br.gov.pb.der.modelo;

import java.io.File;
import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NotaFiscal.class)
public abstract class NotaFiscal_ {

	public static volatile CollectionAttribute<NotaFiscal, ItemNota> itens;
	public static volatile SingularAttribute<NotaFiscal, File> file;
	public static volatile SingularAttribute<NotaFiscal, String> numero;
	public static volatile SingularAttribute<NotaFiscal, Double> valor;
	public static volatile SingularAttribute<NotaFiscal, Calendar> dataConclusao;
	public static volatile SingularAttribute<NotaFiscal, byte[]> arquivo;
	public static volatile SingularAttribute<NotaFiscal, Long> id;
	public static volatile SingularAttribute<NotaFiscal, Calendar> dataEmissao;
	public static volatile SingularAttribute<NotaFiscal, Empresa> empresa;
	public static volatile SingularAttribute<NotaFiscal, String> descricao;

}

