<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    include_once  __DIR__.'./../service/EtudiantService.php';
    loadAll();
}
function loadAll() {
    $es = new EtudiantService();
    header('Content-type: application/json');
    echo json_encode($es->findAllApi());
}