<?php
    if (isset($_POST['code'])) $code = htmlentities($_POST['code']);
    if (empty($code)){
        echo json_encode(array('success'=>'no data'));
    }
    else{
    $link = mysqli_connect("link", "dbname", "dbpass")
        or die("Could not connect : " . mysqli_connection_error());
    mysqli_select_db($link,"tablename") or die("Could not select database");

    $result = mysqli_query ($link,"DELETE FROM codes WHERE code='$code'");
    if ($result){
        echo json_encode(array('success'=>'good'));
    }
    else{
        echo json_encode(array('success'=>'error'));
    }
    mysqli_free_result($result);

    mysqli_close($link);
    }
?>