// 날짜 배열 → "2026-05-27 20:53:25" 변환
function formatDate(arr) {
  if (!Array.isArray(arr) || arr.length < 3) return "-";
  const [year, month, day, hour = 0, min = 0, sec = 0] = arr;
  const pad = (n) => String(n).padStart(2, "0");
  return `${year}-${pad(month)}-${pad(day)} ${pad(hour)}:${pad(min)}:${pad(sec)}`;
}

// 학년 배지
function gradeBadge(level) {
  return `<span class="badge-grade badge-grade-${level}" >${level}학년</span>`;
}

// 학기 배지
function semesterBadge(sem) {
  return `<span class="badge-semester badge-semester-${sem}">${sem}학기</span>`;
}

// 시험 종류 배지
function examBadge(type) {
  if (type === "MID")
    return `<span class="badge-exam badge-exam-mid">중간</span>`;
  return `<span class="badge-exam badge-exam-fin">기말</span>`;
}

async function getStudentsList() {
  try {
    const grade = Number(document.getElementById("grade").value);
    const semester = Number(document.getElementById("semester").value);
    const examType = document.getElementById("examType").value;
    const classNumber = Number(document.getElementById("classNumber").value);
    const schoolGrade = Number(document.getElementById("schoolGrade").value);
    const tbody = document.getElementById("tbodySec");

    let res = await fetch(`${BASE_URL}/api/getStdList`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        grade, // 학년
        semester, // 학기
        examType, // 시험 종류
        classNumber, // 반
        // schoolGrade,
      }),
    });

    res = await res.json();

    tbody.innerHTML = "";

    if (res.result === "fail") {
      tbody.innerHTML = `
        <tr class="table-info-row">
          <td colspan="17" style="text-align: center; padding: 5rem 0; color: #d9534f;">
            <div style="font-size: 1.5rem; margin-bottom: 0.5rem; font-weight: bold;">🚫 검색에 실패하였습니다.</div>
            <p style="font-size: 1rem; color: #999; margin: 0;">관리자에게 문의해주시기 바랍니다.</p>
          </td>
        </tr>
      `;
      return;
    }

    if (!res.data || res.data.length === 0) {
      tbody.innerHTML = `
        <tr class="table-info-row">
          <td colspan="17" style="text-align: center; padding: 5rem 0; color: #d9534f;">
            <div style="font-size: 1.5rem; margin-bottom: 0.5rem; font-weight: bold;">🚫 검색 결과가 존재하지 않습니다</div>
            <p style="font-size: 1rem; color: #999; margin: 0;">조건을 다시 변경하여 검색해 보시기 바랍니다.</p>
          </td>
        </tr>
      `;
      return;
    }

    console.log("진입!");

    // 3. 검색 결과가 존재할 때 (반복문 돌며 화면에 그리기)
    res.data.forEach((student) => {
      const tr = document.createElement("tr");
      tr.onclick = () => goToInfoArea(student);
      tr.innerHTML = `
    <td>${gradeBadge(student.gradeLevel)}</td>
    <td>${student.classNo + "반"}</td>
    <td>${student.studentId || ""}</td>
    <td>${student.studentName || ""}</td>
    <td>${semesterBadge(student.semester)}</td>
    <td>${examBadge(student.examType)}</td>
    <td>${student.gradeRank + "등"}</td>
    <td>${student.classRank + "등"}</td>
    <td>${student.koreanScore.toFixed(2) + "점"}</td>
    <td>${student.mathScore.toFixed(2) + "점"}</td>
    <td>${student.englishScore.toFixed(2) + "점"}</td>
    <td>${student.averageScore.toFixed(2) + "점"}</td>
    <td >${formatDate(student.createdAt)}</td>
    <td >${formatDate(student.updatedAt)}</td>
  `;
      tbody.appendChild(tr);
    });

    console.log(res);
  } catch (error) {
    console.error(error);
  }
}
