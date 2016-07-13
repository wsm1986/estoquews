package br.com.caelum.estoque.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.ResponseWrapper;

import br.com.caelum.estoque.modelo.item.Filtro;
import br.com.caelum.estoque.modelo.item.Filtros;
import br.com.caelum.estoque.modelo.item.Item;
import br.com.caelum.estoque.modelo.item.ItemDao;
import br.com.caelum.estoque.modelo.item.ItemValidador;
import br.com.caelum.estoque.modelo.item.ListaItens;
import br.com.caelum.estoque.modelo.usuario.TokenDao;
import br.com.caelum.estoque.modelo.usuario.TokenUsuario;

@WebService
public class EstoqueWS {

	private ItemDao dao = new ItemDao();

	// Result Personalizado
	@WebMethod(operationName = "todosOsItens")
	@WebResult(name = "itens")
	public ListaItens getItens() {
		System.out.println("Chamando todosItens()");
		List<Item> itens = dao.todosItens();
		return new ListaItens(itens);
	}

	// Muda o nome da request
	@WebMethod(operationName = "todosOsItens2")
	@WebResult(name = "itens2")
	public ListaItens getItens2(@WebParam(name = "filtros") Filtros filtro) {
		System.out.println("Chamando todosItens 2()");
		List<Filtro> lista = filtro.getLista();
		List<Item> itens = dao.todosItens(lista);
		return new ListaItens(itens);
	}

	@WebMethod(exclude = true)
	public void outroMetodo() {
		// nao vai fazer parte do WSDL
	}

	// Resulto Personalidado
	@WebMethod(operationName = "todosOsItens3")
	@ResponseWrapper(localName = "itens")
	@WebResult(name = "itens")
	public List<Item> getItens3() {
		System.out.println("Chamando getItens()");
		return dao.todosItens();
	}

	// cadastrar item
	@WebMethod(operationName = "CadastrarItem")
	@WebResult(name = "item")
	public Item cadastrarItem(@WebParam(name = "token", header = true) TokenUsuario tokenUsuario,
			@WebParam(name = "item") Item item) throws AutorizacaoException {
		System.out.println("Cadastrando Item");
		if (!new TokenDao().ehValido(tokenUsuario)) {
			throw new AutorizacaoException("Autorizacao falhou");
		}
		dao.cadastrar(item);
		
		new ItemValidador(item).validate();
		return item;
	}
}
