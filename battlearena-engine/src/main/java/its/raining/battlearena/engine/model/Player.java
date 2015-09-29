package its.raining.battlearena.engine.model;

public class Player {

  private String nom;

  private int nbrDePieces;

  private String dernierMouvement;

  public String getNom() {
    return this.nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public int getNbrDePieces() {
    return this.nbrDePieces;
  }

  public void setNbrDePieces(int nbrDePieces) {
    this.nbrDePieces = nbrDePieces;
  }

  public String getDernierMouvement() {
    return this.dernierMouvement;
  }

  public void setDernierMouvement(String dernierMouvement) {
    this.dernierMouvement = dernierMouvement;
  }
}
