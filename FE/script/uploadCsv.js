// 페이지 로드 시 드롭다운 자동으로 불러오기
// document.addEventListener("DOMContentLoaded", getDropdown);

// ── 히든 인풋 생성 및 파일 선택 창 열기 ──────
function createHiddenInput() {
  const input = document.createElement("input");
  input.type = "file";
  input.accept = ".csv, text/csv";
  input.style.display = "none";
  document.body.appendChild(input);

  input.addEventListener("change", () => {
    const file = input.files[0];
    if (file) handleFile(file);
    input.remove();
  });

  input.click();
  return input;
}

// ── CSV 검증 ──────────────────────────────────
function checkExtension(file) {
  return file.name.toLowerCase().endsWith(".csv");
}

function checkMime(file) {
  const allowed = [
    "text/csv",
    "application/csv",
    "text/plain",
    "application/vnd.ms-excel",
  ];
  return file.type === "" || allowed.includes(file.type);
}

function checkContent(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();

    reader.onload = (e) => {
      const text = e.target.result.replace(/^\uFEFF/, "");
      if (/[\x00-\x08\x0B\x0C\x0E-\x1F]/.test(text)) {
        return reject(new Error("바이너리 파일입니다."));
      }

      const firstLine = text.split(/\r?\n/)[0];

      if (!firstLine.includes(",") && !firstLine.includes("\t")) {
        return reject(
          new Error("CSV 형식이 아닙니다. 첫 행에 구분자(,)가 없습니다."),
        );
      }
      resolve(true);
    };
    reader.onerror = () =>
      reject(new Error("파일을 읽는 중 오류가 발생했습니다."));
    reader.readAsText(file.slice(0, 1024));
  });
}

async function validateCSV(file) {
  if (!checkExtension(file))
    throw new Error("확장자가 .csv인 파일만 업로드할 수 있습니다.");
  if (!checkMime(file))
    throw new Error(`허용되지 않는 파일 형식입니다. (${file.type})`);
  await checkContent(file);
}

// ── 서버 전송 ─────────────────────────────────
async function uploadToServer(file) {
  try {
    const bodyData = new FormData();
    bodyData.append("file", file);
    bodyData.append("fileName", file.name);

    const response = await fetch(`${BASE_URL}/api/fileUpload`, {
      method: "POST",
      body: bodyData,
    });

    if (!response.ok) {
      throw new Error("서버 응답 실패");
    }

    const data = await response.text();
    return data;
  } catch (error) {
    console.error("업로드 중 에러 발생:", error);
    throw error;
  }
}

// ── 파일 처리 ─────────────────────────────────
async function handleFile(file) {
  try {
    await validateCSV(file);
    await uploadToServer(file);
    alert(`업로드 완료: ${file.name}`);
  } catch (err) {
    alert(`오류: ${err.message}`);
  }
}
