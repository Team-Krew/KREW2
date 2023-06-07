# KREW
건국대 모바일 프로그래밍 1팀 기말 프로젝트

## 프로젝트 파이팅!!

6. 8. 변경사항
-PlacePredictionAdapter 추가
-ScheduleAdapter 추가
-apimodels라는 패키지 추가 (places api가 가져온 정보 필터링하는데 사용하는 model들)
-controller에서 addschedule 거의 완성(아직 날짜 예외처리부분은 처리 안됨.)
-ProgrammaticAutocompleteGeocodingActivity 액티비티 추가(기존의 Findlocation activity를 대체하는 acitivity)
-scheduleVar - 장소 검색을 위한 activity전환시 addschedule xml 값이 초기화 되는걸 방지하기 위한 object 객체
-edittextstyle_left,right 추가 -년월일과 시간이 있는 공간 분리 위해서
-memberlist_checkedimage, memberlist_not_checked_imagexml파일 추가 - gridlayout에서 체크되었는지 체크하고, background image변경을
위해 추가한 xml 파일

-activity_prgrammatic_autocomplete.xml 추가 - 지도검색 레이아웃
-place_prediction_item (recyclerview row들)
-string.xml에 api키 담아두는 string 추가

-androidManifest에 액티비티 추가
-Project 수준 build.gradle, settings.gradle 모듈 수준 build.gradle에 각각 관련된 것 들 implementation

