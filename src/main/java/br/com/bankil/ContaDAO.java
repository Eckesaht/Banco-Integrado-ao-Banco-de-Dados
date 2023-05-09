package br.com.bankil;

import br.com.bankil.domain.cliente.Cliente;
import br.com.bankil.domain.conta.Conta;
import br.com.bankil.domain.conta.DadosAberturaConta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContaDAO {
    private Connection con;
    private DadosAberturaConta dadosDaConta;
    public ContaDAO(Connection conexao, DadosAberturaConta dadosDaConta) {
        this.con = conexao;
        this.dadosDaConta = dadosDaConta;
    }

    public void salvarContaNoBanco() throws SQLException {

        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);

        String sql = "INSERT INTO conta(numero, saldo, cliente_nome, " +
                "cliente_cpf, cliente_email)" +
                "VALUE (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = con.prepareStatement(sql);

        preparedStatement.setInt(1, conta.getNumero());
        preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
        preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
        preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
        preparedStatement.setString(5, dadosDaConta.dadosCliente().email());
        preparedStatement.execute();
    }
}
