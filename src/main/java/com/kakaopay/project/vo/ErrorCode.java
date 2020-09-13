package com.kakaopay.project.vo;

public class ErrorCode {
    public final static class Service {
        public final static String NO_DATA	        = "데이터가 존재하지 않습니다.";
        public final static String ONLY_SHOW_YOURSELF	= "뿌린 사람 자신만 조회할 수 있습니다.";
        public final static String EXPIRE_SHOW	    = "조회 기간이 경과되었습니다.";
        public final static String EXSIST_TOKEN	    = "토큰이 중복 됩니다. 다시 시도해 주세요.";
        public final static String NOT_EXSIST_USER	= "존재하지 않는 유저 입니다.";
        public final static String NOT_EXIIST_ROOM	= "카카오톡 방이 존재하지 않습니다. 방을 생성해주세요.";
        public final static String EXPIRE_RECEIVE	= "받기 유효시간이 경과하였습니다.(10분)";
        public final static String ONLY_GET_OTHER	= "자신이 뿌리기 한 건은 자신이 받을 수 없습니다.";
        public final static String NOT_EXSIST_ROOM_USER	= "카카오톡 방의 참여자가 아닙니다.";
        public final static String MUST_PICKUP_ONCE	= "뿌리기당 한 사용자는 한번만 받을수 있습니다.";
    }
}
