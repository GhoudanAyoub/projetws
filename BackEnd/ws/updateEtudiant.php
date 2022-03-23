<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once '../racine.php';
    include_once RACINE . '/service/EtudiantService.php';
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
    if($img == "no") {
        $image = null;
    }else {
        $image = $img;
    }
    $etd->setImg($image);

    $es->update($etd);
    header('Content-type: application/json');
    //echo json_encode($es->findAllApi());   
}
