<?php
require("koneksi.php");

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $id = $_POST["id"];

    $query = "SELECT FROM mahasiswa WHERE id = '$id'";
    $eksekusi = mysqli_query($konek,$query);
    $cek = mysqli_affected_rows($konek);

    if($cek > 0){
        $response["kode"] = 1;
        $response["pesan"] = "Berhasil";
        $response["data"] = array();

    while($ambil = mysqli_fetch_object($exequery)){
       $F['id'] = $ambil->id;
       $F['nim'] = $ambil->nim;
       $F['nama'] = $ambil->nama;
       $F['jurusan'] = $ambil->jurusan;

       array_push($response["data"],$F);
    }
    }else{
        $response["kode"] = 0;
        $response["pesan"] = "Data tidak tersedia";
    }
}else{
    $response["kode"] = 0;
    $response["pesan"] = "Tidak Ada Post Data";
}

echo json_encode($response);
mysqli_close($konek);
?>