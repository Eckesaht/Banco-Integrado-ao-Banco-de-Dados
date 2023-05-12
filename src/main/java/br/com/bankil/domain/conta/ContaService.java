package br.com.bankil.domain.conta;

import br.com.bankil.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Set;

public class ContaService {

    private ContaDAO acoes;

    private Set<Conta> contas;
    public ContaService() {
        this.acoes = new ContaDAO();
    }


    public Set<Conta> listarContasAbertas() {
        this.contas = this.acoes.listarContas();
        return this.contas;
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) throws SQLException {
        this.acoes.salvarContaNoBanco(dadosDaConta);

    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        BigDecimal novoSaldo = conta.getSaldo().subtract(valor);
        acoes.alterar(conta.getNumero(), novoSaldo);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        BigDecimal novoValor = conta.getSaldo().add(valor);
        acoes.alterar(conta.getNumero(), novoValor);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        acoes.deletarContaDoBanco(numeroDaConta);
    }

    private Conta buscarContaPorNumero(Integer numero) {
        return contas
                .stream()
                .filter(c -> c.getNumero().equals(numero))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Não existe conta cadastrada com esse número!"));
    }

    public void realizarTransferencia(int numeroDaContaOrigem, int numeroDaContaDestino, BigDecimal valor) {
        this.realizarSaque(numeroDaContaOrigem, valor);
        this.realizarDeposito(numeroDaContaDestino, valor);
    }
}
