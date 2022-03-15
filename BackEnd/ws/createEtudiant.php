<?php
if($_SERVER["REQUEST_METHOD"] == "POST"){
    chdir("..");
    include_once __DIR__.'./../service/EtudiantService.php';
    create();
}
function create(){
    extract($_POST);
    $es = new EtudiantService();
    $es->create(new Etudiant(1, $nom, $prenom, $ville, $sexe));
    header('Content-type: application/json');
    echo json_encode($es->findAllApi());   
}