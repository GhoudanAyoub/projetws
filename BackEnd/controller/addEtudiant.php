<?php
include_once __DIR__.'./../service/EtudiantService.php';
extract($_GET);
$es = new EtudiantService();
$es->create(new Etudiant(1, $nom, $prenom, $ville, $sexe));
header("location:../index.php");