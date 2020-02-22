package br.gov.pb.der.modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ItemNota.class)
public abstract class ItemNota_ {

	public static volatile SingularAttribute<ItemNota, Double> valor;
	public static volatile SingularAttribute<ItemNota, String> nome;
	public static volatile SingularAttribute<ItemNota, Long> id;
	public static volatile SingularAttribute<ItemNota, NotaFiscal> notaFiscal;
	public static volatile SingularAttribute<ItemNota, Integer> quantidade;
	public static volatile SingularAttribute<ItemNota, Setor> setorDestino;

}

