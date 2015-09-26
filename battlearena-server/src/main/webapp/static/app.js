(function() {
  var socket = new WebSocket("ws://localhost:8080/battlearena-server/ws");

  var State = {
    idEquipe: null,
    idPartie: null
  }

  function log(msg) {
    var div = $(".logger").find("div");

    div.html(div.html() + "<p>&gt; " + msg + "</p>");
  }

  function handleConnecter(data) {
    State.idEquipe = data.idEquipe;
    log("Connexion réussie, idEquipe = " + data.idEquipe);
    $("#id-equipe").html(data.idEquipe);
  }

  function handleTraining(data) {
    State.idPartie = data.idPartie;
    log("Démarrage partie contre IA, idPartie = " + data.idPartie);
    $("#id-partie").html(data.idPartie);
  }

  function handleVersus(data) {
    State.idPartie = data.idPartie;
    log("Démarrage partie contre équipe, idPartie = " + data.idPartie + ", idAdversaire = " + data.idAdversaire);
    $("#id-partie").html(data.idPartie);
  }

  function handleError(data) {
    log("Erreur reçue = " + data.message);
  }

  /**
   * Configuration du handler à la réception d'un message
   */
  socket.onmessage = function(event) {
    console.log(event);

    var data = JSON.parse(event.data);

    switch (data.type) {
    case "ConnecterResponse":
      handleConnecter(data);
      break;
    case "TrainingResponse":
      handleTraining(data);
      break;
    case "VersusResponse":
      handleVersus(data);
      break;
    case "ErrorResponse":
      handleError(data);
      break;
    default:
      log("Action " + data.type + " non géré");
    }

    console.log(data);
  };

  $(document).ready(function() {
    log("chargement terminé");

    $("#connecter").click(function() {
      var nomEquipe = $("#nom-equipe").val();
      var motDePasse = $("#mot-de-passe").val();

      socket.send(JSON.stringify({
        type: ".ConnecterMessage",
        nomEquipe: nomEquipe,
        motDePasse: motDePasse
      }));
    });

    $("#training").click(function() {
      socket.send(JSON.stringify({
        type: ".TrainingMessage",
        level: $("#training-level").val(),
        idEquipe: State.idEquipe
      }));
    });
    
    $("#versus").click(function() {
      socket.send(JSON.stringify({
        type: ".VersusMessage",
        idEquipe: State.idEquipe
      }));
    });
  });
})();
