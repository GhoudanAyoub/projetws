<?php
include_once '../racine.php';
include_once RACINE . '/service/EtudiantService.php';
extract($_POST);
$es = new EtudiantService();

$uploadedImg = null;
if (isset($_FILES["img"]["name"]) && !empty($_FILES["img"]["name"])) {
    $target_dir = '../images/';
    $target_file = $target_dir . basename($_FILES["img"]["name"]);
    $check = getimagesize($_FILES["img"]["tmp_name"]);
    if ($check !== false) {
        $uploadedImg = file_get_contents($_FILES['img']['tmp_name']);
    }
}

$es->create(new Etudiant(1, $nom, $prenom, $ville, $sexe, $uploadedImg));
header("location:../index.php");

