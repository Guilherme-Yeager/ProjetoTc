public interface OperacoesAutomatoInterface {

    // 1. Devera tratar de uma ferramenta que importe dois AFD/N no formato .jff e realize a operacao regular desejada, sendo ela: uniao;
    public void aplicarUniaoAFN(Automato automato1, Automato automato2);

    public void aplicarUniaoAFD(Automato automato1, Automato automato2);
}