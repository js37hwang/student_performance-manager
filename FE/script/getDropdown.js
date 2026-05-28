const API = {
  attendance: "/api/upload/attendance",
  grade: "/api/upload/grade",
};

// ── 드롭다운 데이터 가져오기 ──────────────────
async function getDropdown() {
  try {
    const res = await fetch("http://127.0.0.1:8181/api/getDropdown", {
      method: "POST",
    });

    // response -> res로 수정
    if (!res.ok) throw new Error("서버 응답 실패");

    const data = await res.json();

    // res.status 대신 res.ok를 통과했다면 로직을 진행하도록 변경 (혹은 백엔드 응답 조건에 맞추기)
    if (data) {
      let searchObj = [
        "grade",
        "semester",
        "examType",
        "classNumber",
        "schoolGrade",
      ];

      for (let i = 0; i < searchObj.length; i++) {
        let dataArr = data[searchObj[i]];

        // 서버에서 해당 키의 데이터가 내려오지 않았을 때를 대비한 예외 처리
        if (!dataArr) continue;

        let dropdownElement = document.getElementById(searchObj[i]);
        if (!dropdownElement) continue;

        for (let j = 0; j < dataArr.length; j++) {
          dropdownElement.innerHTML += `<option value="${dataArr[j].value}">${dataArr[j].label}</option>`;
        }
      }
    } else {
      console.log("데이터 받아오기 실패");
    }
  } catch (error) {
    console.error(error);
  }
}
