function goToInfoArea(stdInfo) {
  try {
    const inputArr = [
      "studentId",
      "studentName",
      "semester_i",
      "examType_i",
      "koreanScore",
      "mathScore",
      "englishScore",
    ];

    for (let i = 0; i < inputArr.length; i++) {
      let val = stdInfo[inputArr[i]];

      switch (inputArr[i]) {
        case "examType_i":
          val = stdInfo["examType"] === "MID" ? "중간" : "기말";
          document.getElementById(inputArr[i]).name = stdInfo["idx"];
          break;
        case "semester_i":
          val = stdInfo["semester"] + "학기";
          break;
        default:
          break;
      }
      document.getElementById(inputArr[i]).value = val;
    }
  } catch (error) {
    console.error(error);
  }
}

async function editStdScore() {
  try {
    let idx = Number(document.getElementById("examType_i").name);
    let studentId = document.getElementById("studentId").value;
    let korean_score = Number(document.getElementById("koreanScore").value);
    let math_score = Number(document.getElementById("mathScore").value);
    let english_score = Number(document.getElementById("englishScore").value);

    if (!idx || !studentId) {
      alert("학생을 선택해주세요.");
      return;
    }

    if (
      korean_score < 0 ||
      korean_score > 100 ||
      math_score < 0 ||
      math_score > 100 ||
      english_score < 0 ||
      english_score > 100
    ) {
      alert("시험 점수는 0점부터 100점 사이의 값만 입력 가능합니다.");
      return;
    }

    let res = await fetch(`${BASE_URL}/api/editStd`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        stdInfo: {
          idx,
          studentId,
        },
        scoreList: {
          korean_score,
          math_score,
          english_score,
        },
      }),
    });
    res = await res.json();

    if (res.result === "fail") {
      alert("학생 성적 수정에 실패했습니다. 관리자에게 문의해주세요.");
      return;
    }
    await getStudentsList();
    alert("학생 성적이 수정되었습니다.");
  } catch (error) {
    console.error(error);
  } finally {
    document.getElementById("examType_i").name = null;
    document.getElementById("studentId").value = null;
    document.getElementById("koreanScore").value = null;
    document.getElementById("mathScore").value = null;
    document.getElementById("englishScore").value = null;
  }
}

async function delStd() {
  try {
    let idx = Number(document.getElementById("examType_i").name);
    let studentId = document.getElementById("studentId").value;
    let korean_score = Number(document.getElementById("koreanScore").value);
    let math_score = Number(document.getElementById("mathScore").value);
    let english_score = Number(document.getElementById("englishScore").value);

    if (!idx || !studentId) {
      alert("학생을 선택해주세요.");
      return;
    }

    let res = await fetch(`${BASE_URL}/api/delStd`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        idx,
        studentId,
      }),
    });
    res = await res.json();

    if (res.result === "fail") {
      alert("학생 정보를 삭제에 실패했습니다. 관리자에게 문의해주세요.");
      return;
    }
    await getStudentsList();
    alert("학생 정보를 삭제하였습니다.");
  } catch (error) {
    console.error(EvalError);
  } finally {
    document.getElementById("examType_i").name = null;
    document.getElementById("studentId").value = null;
    document.getElementById("koreanScore").value = null;
    document.getElementById("mathScore").value = null;
    document.getElementById("englishScore").value = null;
  }
}
