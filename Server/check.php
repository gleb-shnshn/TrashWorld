<?php
    if (isset($_POST['code'])) $code = htmlentities($_POST['code']);
    if (empty($code)){
        echo json_encode(array('success'=>'no data'));
    }
    else{
    $link = mysqli_connect("link", "dbname", "dbpass")
        or die("Could not connect : " . mysqli_connection_error());
    mysqli_select_db($link,"tablename") or die("Could not select database");

    $query = "SELECT * FROM codes WHERE code='$code'";
    $result = mysqli_query($link, $query) or die("Query failed : " . mysqli_error());
    $s=0;
    if ($row = mysqli_fetch_assoc($result)) {
        $b=['success' => 'good'];
    }
    else{
        $b=['success' => 'bad'];
    }
    $json = json_encode($b);
    echo $json;

    mysqli_free_result($result);

    mysqli_close($link);
    }
?>