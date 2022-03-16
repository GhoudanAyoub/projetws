<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    chdir("..");
    include_once __DIR__ . './../service/EtudiantService.php';
    update();
}
function update()
{
    extract($_POST);
    $es = new EtudiantService();
    $etd = $es->findById(intval($id));
    $etd->setNom($nom);
    $etd->setPrenom($prenom);
    $etd->setVille($ville);
    $etd->setSexe($sexe);
    $es->update($etd);
    header('Content-type: application/json');
    //echo json_encode($es->findAllApi());   
}
