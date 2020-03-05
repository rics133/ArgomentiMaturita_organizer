package it.rics.app;

public class Argomenti {
  private int id;
  private String NomeArgomento;
  private String Riferimento;

  public Argomenti(int id, String nomeArgomento, String riferimento) {
    this.id = id;
    NomeArgomento = nomeArgomento;
    Riferimento = riferimento;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNomeArgomento() {
    return NomeArgomento;
  }

  public void setNomeArgomento(String nomeArgomento) {
    NomeArgomento = nomeArgomento;
  }

  public String getRiferimento() {
    return Riferimento;
  }

  public void setRiferimento(String riferimento) {
    Riferimento = riferimento;
  }
}
