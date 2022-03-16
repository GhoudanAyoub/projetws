<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    chdir("..");
    include_once __DIR__ . './../service/EtudiantService.php';
    delete();
}
function delete()
{
    extract($_POST);
    $es = new EtudiantService();
    $es->delete($es->findById(intval($id)));
    header('Content-type: application/json');
    echo json_encode($es->findAllApi());
}
