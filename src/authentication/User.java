package authentication;

public class User {
    private int id;
    private String nome;
    private String email;
    private int nivelPermissao;
    private String bioFacial;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getPermission() { return nivelPermissao; }
    public void setNivelPermissao(int nivelPermissao) { this.nivelPermissao = nivelPermissao; }

    public String getBioFacial() { return bioFacial; }
    public void setBioFacial(String bioFacial) { this.bioFacial = bioFacial; }
}

