package com.example.honeybee;

public class dump {
//    리사이클러뷰 이미지 경로저장 갤러리에서
//       if(data == null){   // 어떤 이미지도 선택하지 않은 경우
//        Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
//    }
//                    else{   // 이미지를 하나라도 선택한 경우
//        if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
//            Log.e("single choice: ", String.valueOf(data.getData()));
//            Uri imageUri = data.getData();
//            uriList.add(imageUri);
//
//            adapter = new MultiImageAdapter(uriList, getApplicationContext());
//            recyclerView.setAdapter(adapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
//        }
//        else{      // 이미지를 여러장 선택한 경우
//            ClipData clipData = data.getClipData();
//            Log.e("clipData", String.valueOf(clipData.getItemCount()));
//
//            if(clipData.getItemCount() > 9){   // 선택한 이미지가 11장 이상인 경우
//                Toast.makeText(getApplicationContext(), "사진은 9장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
//            }
//            else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
//                Log.e(TAG, "multiple choice");
//
//                for (int i = 0; i < clipData.getItemCount(); i++){
//                    Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
//
//                    num = String.valueOf(i);
//
//                    try {
//                        uriList.add(imageUri);  //uri를 list에 담는다.
//                        uriL = String.valueOf(imageUri);
//                        //bitmap
//                        bp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                        bpL = imageToString(bp);
//                        jsonObject(num,bpL);
//                    } catch (Exception e) {
//                        Log.e(TAG, "File select error", e);
//                    }
//
//                }
//
//            }
//        }
//    }
}
