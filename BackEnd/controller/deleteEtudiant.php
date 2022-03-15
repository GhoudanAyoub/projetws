<?php
include_once __DIR__.'./../service/EtudiantService.php';
extract($_GET);
$es = new EtudiantService();
$es->delete($es->findById($id));
header("location:../index.php");