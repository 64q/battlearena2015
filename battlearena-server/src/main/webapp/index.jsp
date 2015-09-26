
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Battlearena 2015</title>
<link rel="stylesheet" href="<c:url value="/static/app.css" />">
</head>
<body>
  <header>
    <h1>Battlearena 2015</h1>
  </header>

  <div class="container">

    <!--  -->
    <div class="item mission-control">
      <h2>Connexion</h2>
      <div>
        Nom équipe: <input type="text" id="nom-equipe" />
      </div>
      <div>
        Mot de passe: <input type="text" id="mot-de-passe" />
      </div>
      <div>
        <button id="connecter">Connexion équipe</button>
      </div>
      
      <h2>Informations</h2>
      <ul>
        <li>Id équipe: <span id="id-equipe">Non connecté</span></li>
        <li>Id partie: <span id="id-partie">Pas en partie</span></li>
      </ul>
    </div>
    
    <div class="item logger">
      <h2>Logger</h2>
      <div></div>
    </div>
    
    <div class="game-actions item">
      <h2>Actions</h2>
      <div>
        <p>
          IA: <input type="text" value="easy" id="training-level" /> <button id="training">Commencer</button> <button id="training-stop">Stopper</button>
        </p>
        <p>
          Versus: <button id="versus">Commencer</button>
        </p>
        <p>
          Infos: <button id="recuperer-infos">Récupérer état</button>
        </p>
      </div>
    </div>
  </div>

  <script src="<c:url value="/static/json2.min.js" />"></script>
  <script src="<c:url value="/static/jquery-1.11.3.min.js" />"></script>
  <script src="<c:url value="/static/app.js" />"></script>
</body>
</html>