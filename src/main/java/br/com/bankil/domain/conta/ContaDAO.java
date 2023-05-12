package br.com.bankil.domain.conta;

import br.com.bankil.ConnectionFactory;
import br.com.bankil.domain.cliente.Cliente;
import br.com.bankil.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {
    private Connection conexao;

    protected void salvarContaNoBanco(DadosAberturaConta dadosDaConta) throws SQLException {
        this.conexao = new ConnectionFactory().retornarConexao();

        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente, BigDecimal.ZERO, true);

        String sql = "INSERT INTO conta(numero, saldo, cliente_nome, " +
                "cliente_cpf, cliente_email)" +
                "VALUE (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = conexao.prepareStatement(sql);

        preparedStatement.setInt(1, conta.getNumero());
        preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
        preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
        preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
        preparedStatement.setString(5, dadosDaConta.dadosCliente().email());
        preparedStatement.execute();

        preparedStatement.close();
        this.conexao.close();
    }

    protected void deletarContaDoBanco(Integer numero) {
        this.conexao = new ConnectionFactory().retornarConexao();
        String sql = "DELETE FROM conta WHERE numero = ?";

        try {
            this.conexao.setAutoCommit(false);
            PreparedStatement preparedStatement = this.conexao.prepareStatement(sql);
            preparedStatement.setInt(1, numero);

            preparedStatement.execute();
            this.conexao.commit();
            this.conexao.close();
            preparedStatement.close();

        } catch (SQLException e) {
            try {
                this.conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    protected Set<Conta> listarContas() {
        this.conexao = new ConnectionFactory().retornarConexao();
        Set<Conta> contas = new HashSet<>();
        String sql = "SELECT * FROM conta WHERE esta_ativa = true";

        try {
            PreparedStatement preparedStatement = this.conexao.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer numero = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String cliente_nome = resultSet.getString(3);
                String cliente_cpf = resultSet.getString(4);
                String cliente_email = resultSet.getString(5);
                Boolean esta_ativa = resultSet.getBoolean(6);

                DadosCadastroCliente dadosNovoCliente = new DadosCadastroCliente(cliente_nome, cliente_cpf, cliente_email);
                Cliente novoCliente = new Cliente(dadosNovoCliente);
                contas.add(new Conta(numero, novoCliente, saldo, esta_ativa));

            }
            preparedStatement.close();
            resultSet.close();
            this.conexao.close();
        } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        return contas;
    }

    protected void alterar(Integer numeroDaConta, BigDecimal valor) {

        this.conexao = new ConnectionFactory().retornarConexao();
        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";

        try {
            this.conexao.setAutoCommit(false);
            PreparedStatement preparedStatement = conexao.prepareStatement(sql);

            preparedStatement.setBigDecimal(1, valor);
            preparedStatement.setInt(2, numeroDaConta);
            preparedStatement.execute();
            this.conexao.commit();
            this.conexao.close();
            preparedStatement.close();

        } catch (SQLException e) {
            try {
                this.conexao.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
        }
            throw new RuntimeException(e);
        }
    }
}
