<?php
include_once '../racine.php';
include_once RACINE . '/service/EtudiantService.php';
extract($_POST);
$es = new EtudiantService();

if (isset($_FILES["img"]["name"]) && !empty($_FILES["img"]["name"])) {
    $target_dir = '../images/';
    $target_file = $target_dir . basename($_FILES["img"]["name"]);
    $check = getimagesize($_FILES["img"]["tmp_name"]);
    if ($check !== false) {
        $uploadedImg = file_get_contents($_FILES['img']['tmp_name']);
        if (move_uploaded_file($_FILES["img"]["tmp_name"], $target_file)) {
            $es->create(new Etudiant(1, $nom, $prenom, $ville, $sexe, $uploadedImg));
            header("location:../index.php");
        }
    }
}

