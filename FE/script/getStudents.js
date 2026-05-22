async function getStudentsList() {
  try {
    const grade = document.getElementById("grade").value;
    const semester = document.getElementById("semester").value;
    const examType = document.getElementById("examType").value;
    const classNumber = document.getElementById("classNumber").value;
    const schoolGrade = document.getElementById("schoolGrade").value;

    const res = await fetch("http://127.0.0.1:8181/api/getStudentsList", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        grade,
        semester,
        examType,
        classNumber,
        schoolGrade,
      }),
    });

    if (!data.lists || data.lists.length === 0) {
      tbody.innerHTML = `
        <tr class="table-empty-row">
          <td colspan="17" style="text-align: center; padding: 5rem 0; color: #d9534f;">
            <div style="font-size: 1.5rem; margin-bottom: 0.5rem; font-weight: bold;">🚫 검색 결과가 존재하지 않습니다</div>
            <p style="font-size: 1rem; color: #999; margin: 0;">조건을 다시 변경하여 검색해 보시기 바랍니다.</p>
          </td>
        </tr>
      `;
      return;
    }

    // 3. 검색 결과가 존재할 때 (반복문 돌며 화면에 그리기)
    data.lists.forEach((student) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${student.student_code || ""}</td>
        <td>${student.student_name || ""}</td>
        <td>${student.semester_name || ""}</td>
        <td>${student.exam_name || ""}</td>
        <td>${student.total_grade_avg || ""}</td>
        <td>${student.KOR_01 || "0"}</td>
        <td>${student.KOR_01_grade_avg || "-"}</td>
        <td>${student.MATH_01 || "0"}</td>
        <td>${student.MATH_01_grade_avg || "-"}</td>
        <td>${student.ENG_02 || "0"}</td>
        <td>${student.ENG_02_grade_avg || "-"}</td>
        <td>${student.create_date || "-"}</td>
        <td>${student.edit_date || "-"}</td>
      `;
      tbody.appendChild(tr);
    });

    console.log(res);
  } catch (error) {
    console.error(error);
  }
}
