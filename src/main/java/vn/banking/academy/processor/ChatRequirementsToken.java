package vn.banking.academy.processor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

public class ChatRequirementsToken {

    /**
     * @param accessToken : token của tài khoản chatGpt
     * @return : trả về token được generate để tiến hành chat
     */
    public String generator(String accessToken, String conversationId) {
        String token;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"conversation_mode_kind\":\"primary_assistant\"}");
            Request request = new Request.Builder()
                    .url("https://chat.openai.com/backend-api/sentinel/chat-requirements")
                    .method("POST", body)
                    .addHeader("accept", "*/*")
                    .addHeader("accept-language", "en-US,en;q=0.9,vi;q=0.8")
                    .addHeader("authorization", "Bearer " + accessToken)
                    .addHeader("content-type", "application/json")
                    .addHeader("cookie", "oai-did=2ca588c2-6439-4ca8-9234-2044f5e44d12; intercom-device-id-dgkjq2bp=769fc77b-dc98-42da-9fde-81c946c9e361; ajs_user_id=user-4cUmQw6PITwFV4URsS8jN3uI; ajs_anonymous_id=65dd9696-4e15-4aa0-b906-15f3c729f073; cf_clearance=89G5B1AA8js7qUqOF9kbeTQBnRRoAU0lvwm7EkWjszk-1711037805-1.0.1.1-..8ITWtR93Km.cR383O91zyjPNgmVOepbE7EqMvH211ROrypmVLQvKyzQg9avhZRnt38eao5LQrCcGGg.yFyyQ; __Host-next-auth.csrf-token=447e2e4cc7375ea5ed26b2e70c33d3ef2782adb1a08eae15b7246aef06468d84%7C4b3a8b5c33464495b506da1f029e6d7b47c35571670130fcbbce6c074572ade8; _cfuvid=heKZNa7.AbH6aYJMp15hkt7FLX4CjBw1npiERH28W1Y-1711119468845-0.0.1.1-604800000; cf_clearance=SN9sXq3ZpxvHbhAdkuFlCE6UcLAgS5VxLcQ6nmigNso-1711119470-1.0.1.1-Fl2aXDoGVD4L3heyLB.mj24viqarK8F0zYa8FuJZNeVO.SFJaPeeQIlWs4jePRU1y009qDJbWMVmMffYdz0Fpw; __cflb=0H28vVfF4aAyg2hkHEuhVVUPGkAFmYvk7Jt2eqW47F3; intercom-session-dgkjq2bp=cWZoVXNPMmJkUlpOallGcEUrd2g2NXYvMU14YzAzOHgvRlVSOW9pOER4SlB1SUJEcVFNOVBFMVhKK3prOXhiSy0tOGsvczZuekVCV1ZjcklZQ0pBZzlVQT09--7fd50befd9e3bc293daeb8fe88370bcef67180be; _uasid=\"Z0FBQUFBQmxfYXpyTTRiOE8wSmhzeFBGNXBYWVRGdVZVM1lKS0ZIVWNoVWMtclV0MmJ2VE5KZHlSbzAxclVWcHhFcFZXS05UdGM3ZDdva09ZYTNwWUpEd1paXzhDYmEtb1k2eWYzY2NvTjZhcE9QQUdMMm0ycUF3c2c5ZmZMWktJV0ViQnR4eGg3eVVJaXotbTBhcUNDOVA1UGZ3ZTB3UU9mQUxnYTRfOTdiTFJzYlRkY3RHRV9EaTlrMjRpVlZ6MExLRnpDQTVXSWsyU0ZOd25nd0hzcU41ZnJRWVlQcWp5dkMteHVDRmpfaEtiVGl1X2s5S2laMDBHZElKNFhaUDJFMnFxWUk0aDJZcnM0NDZoMkJBdHZxbzVURnV5X2lmN1N4VUo2VUFxODdibnhxT2JEMTI4S1pSUFpKQ1hOTUNJcGM0M2xXNjB0ay1yZmFvdktNblUwWnNfS01OSWJyOHJ3PT0=\"; _umsid=\"Z0FBQUFBQmxfYXpyR19HRzlkMUdvT2VKeWhPSmFpY2VIbkJVRDZTTTQzbVpNN0ZaVzB4NUxwenBRSDdldC05N0pxLVZIc01NSC1xNTM0a0k5M2VFTkhlZXlQVGlUSUp6MS1IMzlPVU1rX3czMkVOOGhoSS00eDFha25Qd1JSdnJJWG01aElfQm9pbnZXX0hReWd1MnJHMmZuaDY2UU9oUDFsZmprUkRhUmpYUFVIdVMxNmduNlNfZ1h5YVltN1cyUklVOWhRd2tiVklaSXQ3YVdkQ3lMNzFYa3NqWm00RVlHRmhUWHc1TkNjZEpYcElqZmViVTEwND0=\"; __cf_bm=6ulVqjz7sv.OEcTjUBdmFPi.5EcvXiWDClAcpfiBgpU-1711123691-1.0.1.1-wsPOR2q61TuKPFKbPmeLnPQMYJdPg8.XZa05JGWz.Dh7bGR9ccFvSACsXNJ7iPJA2PDklFq3aDBvCM7XoOd_5A; _dd_s=rum=0&expire=1711125047194; __Secure-next-auth.callback-url=https%3A%2F%2Fchat.openai.com; __Secure-next-auth.session-token=eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIn0..XI9XXmXGnJFUgbnh.Arjw4DHCHMwvCOzwrHVr3FGjRuQcVcEho_Xss3y6rWvl3D4rBcc868NKGE9lhUwGpEjCv1qRriVhIu4MOP7xYuKmI6z7AULLNgr2_rgou-hBXsd8xd2Z9HKm6RBc0AbfvfcUJWzHp7mAPpMAJ3sZgWGnJnlduK81eRCvg31CdnE9KzqRlprESmYhKDZTwwfYpIJu68j2NdgjDeM8fQLBWAXDapRste8WbDMRs0EbTRS77w4ksdv8p_aNrgERFQ-uy_TgOJVAfSfNpEyqGDrOkw-DC0e7A3YJGT5z6261zUq98akDd9G06Tdntt-8sB_S9xSPAdg83m86tvfMChQZqEEs_dkwynSaKFYSWdhvXMPxs8R3DdxORkEbkUcMFOlPbA7i7ZSjgy8-9-ypJXA-iZIaSIAjUQV4h8E-cwQxdRDBMtrEXFcUTIFHLOuLqiJ9b3b7jbLmabEyD-WKb6tl164jVa7k-T7PLoa4gmuQZeEEd-_yPVswankBLyOUAOJBkvNbz2uS5dt9IBR6JMV-Djgmn3EXDJhZJfurWcRF6s5auz-dDSe39j1diPNnL0PVNtwynDDhElhHuUv3PSfgPTQPgEo74V0sRtoI_ktGNXDcB_uaw7ODiZG8-zv8mFCpef27Lu0eEK0Xp28frfxFRUNpq02gtFWfJx0_PTFbg78BIoiSE0-v0nWkKQVvcjUih8QFjXnzX2r3L6pidF9p5yErsziLS8ZhPLbq7i_Q08Zl3f7rr0zaC3h44f_QmrBdCRJMcjrIMeei13al01cXdfwhcMZHhfnbHdSafkT5eyPnGo6-icOvn_icxtKfQsZ2AcJEeWleBZq5wX_WkU9c3VCI1sMl7SvQInwR7AL-yXkqeRDQBFqZzc5WwXxekhi8iAfHcnB3JW7cvU1xE9zizJasPsgar2LdEVFDmLUm7iFQnngN0dIln8wF9Oe-SGsgJcI4NBFo2h4VPpwzdOHEQ6ZqUH2r2ywCnXDImqAHe2kfc0F0tMe6vVjDcsD2k9YhJ73VAuyLIaVEbb2w9vGPhkfRlqoPvWzjB1boXqBy_pkrq-lCmBIUYO7wXogo6rTXrcg5-H9hqPS7g7r7QypXbfaQs6oIHVUTR001PUnsJKU3XgFhE_9Ppyz0tfwRsCFFUbLzw-l7Toh-lCaRniWM-8RcW_dkm3xmf1Plc3jXvCIHCMoY_KOHnd5f3qqUD4bmynjoPxn36dgxBVwrpQ65Izcj9ubFST0mO5l0xrtG3aahafdrfYgx8EE4BxYilgioFEL-7k6dnOW8SL02tpN-2JbGrDTO7Ps1vVpBScF2aMqtYPzlTVLqldunUz6_K0J9cw2JxaEQ0vWk7K5gxK7om_bHf-mmRziflCdyLypDxtB5kCahCqE-o5HG-ocv_pYQrmJn4QrtHifLWNrreZVWYrehFS6cqPQzfmhTvrl4CyTnk36bpfM0G-FQ7TsV-hvGxhKB_Cgzj-M1JONTPKXwA25-dhJd93ZmIoH3a_xlbfOJg9lUOhSQs5b07T6JIFXR6ldmuphdyngrOPJ0Z_O6QtWjvbcOoiN2maK8e-uTizZgfdJswCr_nBcOqnAHUY7Fn0obN7EzKxW49TXZoelBMmMgsQxFjU3eYv-gF569_ocmymlCLx2mQB7hl00KCAU6KeLzaZV-deyXtZvq5ZTFhj49bpo2hTm3fp_GURdGKLWBnr-O3rn3NGlO80G8qPprJ-Qtksqf1dGW1F_zn8iQf7_HRZWFalxHJAMm6JZ-DsyRfbNidOeEg_BnSx-zbxkKv9jvwB0BqK3nnIG9WptCzyEkwm9WGwYFZwnrhxJBximWKngsyYC4F-OOT5cpAtbPgl5BfhkKIxKr7OuBmhze7BNCIZU5knCPOt_m-Hmh7RI1xhL_FaMT_v7Y1sV3SFM9_Au5KZz26JtatvByr7LM9eaRdXXbfFdSLMtJ9VOu5AiMhAuglFcZUGtrKgy5IFYfDwWIJ0dS_wzDQCAmPiHw8d_tdn7tJV1hDnMUItON29ZZt_9S5yKXOs14w39l-T0m7w0Gx0y6ps8JBUDjU6dOQsEIsvpPXMPlc2ICBBuhAvZw7M98nxC5PjEr6_bXsjZ1EQvoajbMTIAK1BkYrYFj6wZjTUW8d386F7L4t60IJWxMl3RZUj-1Uf6nCzeH7s1m2PxJPOwhvt8DxJSZAez8ahuG1UwmFs5phuClpR5HFsDuUf3g-5xYh_ItvsZopthYYKWdj4TEVmoDSDxpzDO5U-XB4ycyxk5YBVyh73PVMAigjfOy27s1TD7bQTtXO3px38hLZ7nlm218LmDbMCPj8mQUrKHjt6u7s12t3fw_MESRdtrVpmHhyv_j06RhwpfI35s6RSyD3wKOD3IfY1rtWoPbU--V-RHG7eP6HLyh6dcCBdIuIUP2CD8QI-an1rixBF1SRUViDiGaSmdfq8e8jBHqkcuxWhw6OoprIi2tMh_Jc93SybZLmvxmDfyOx8dAIKwheMXk0CUH6XPgoPqDmOx1gGTVeOO8A3DWAek94HrX4JiJ0tj9EtwnyH_BlX9xg17csihjA5Kszt68obQzuT6TDLw18cHoGz1hjKGUYoDkT8_E8iwHA3ijeryrov5x5TKJ19ZFvEbXWm8A8oaaERZ7H64xBKAiqcdZVIK5rEgU_Zt75ttLdv3lo_O-IVGZrNocWM9wyLPOa_p_GG86BC8OfIFBHtLlDWcGOpOcysgC8CYPxMSGZb0xyWGyDTK83fmaO1mkm_8aROoZHXj46le_ILb-e3rStm0gOHOdfIekjDJ4obyZs92D8nEtGhPzYz8ZnUeQl0q3ED_iUYEGCZVRfoIfiSkilXaoFEoi6RN_MKfOxNWNaxe74zy9A0g0Jm91soNruxo0kHqnXatMJD5fo59gzuyAinLZ--1WcZR04wpjrj3JpJ7h9oqfDLtpMjy_g_k.TPqoClYhy6XtOVXGXsk27Q; __cf_bm=YTsr1dXji_bxk4A_jUU53178tS17ISw4GfTrlV8TJfM-1711125049-1.0.1.1-dmJlJuR.fTm_.N0lZuUFbeqha.W6sNsFF9YioQfs_YWsyJBtE43NvceAg9vjFwU2Cd0Ks4fTrdRVjmTVR7cAbg; __cflb=0H28vVfF4aAyg2hkHEuhVVUPGkAFmYvkAVjaeMmzVUR; _cfuvid=C.Cc9Pt5TtyAARKfar0M4a9Vhd7Wt6XeN5EJa0U1CRA-1711123374766-0.0.1.1-604800000; _uasid=\"Z0FBQUFBQmxfYkdpSjRMUGR6X2U3bFJWc0stanIzZERwUzlzbGNSSmJmclBYc0FaRHFiQy1zdkx6eXFpNjFIUGlKNXdRTFlXRTFhOVdlczZaMDQ2aE9kZDlvaHdBZDJla1Rxcm5ZcnFYQjhHSnp2eWxmSFRTZkx5M19Mb3Jac1pMV3RhZ3ZGRHc5cV9yTVN1TnZMVVRoVWVxRXVFaDVKXzVPY0k3YzN4ZHpTNXZ6aGtzdGwyM3pMdFZyUHpJcVBGQ01hWFNScVYzaXNubWc2Wk5RRElfblFmVzd1TVdPcmhwb1E1bEh0Y2dMT0JSTFdEdzl2RG5LdllZODEzSDIxTTZNdHJodjMxRUo3QXVvWVIzU2t4Y0hEcWZ0d19KeWQ4LWhVRi1nU0h4QldvOHBQaVBqR3BvRldDS3Y2eEhGenV3bVI2RXMxNzZEYk1Ra2ZmUVpXZEc2WHQ3R2xneVoxUkN3PT0=\"; _umsid=\"Z0FBQUFBQmxfYkdpWC02SnU3WVAzdUM4LV9YQ1o1Vm5YY0hVTjVCYWtXejF0UGRQc2pCQWFERTFIYVNxb3BlVlhwMG9tdV9kRkppb0s0NHFPTm8tUlJOTDZxNnZhaEZLOFVXRTI1cS1tRzBQX1VndmdPOTA2eVN5QTJjbV9TeVc4YnNCM0N0ampCXzNDR282M2czbi1hUEVsWG4tZ0l2aHYxbG5xbjkwYWFiaGFFdXV5LXZMVzZjZVkyVUU2UDMxcXVOTS1SbkI2WUs0NUFWblJfZ1o3X2lUeVhsM1locjdwVmtCWWl1eFc5Q0dRalJJclo4WnU0bz0=\"")
                    .addHeader("oai-device-id", "2ca588c2-6439-4ca8-9234-2044f5e44d12")
                    .addHeader("oai-language", "en-US")
                    .addHeader("origin", "https://chat.openai.com")
                    .addHeader("referer", "https://chat.openai.com/c/" + conversationId)
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            JsonObject object = new Gson().fromJson(res, JsonObject.class);
            token = object.get("token").getAsString();
        } catch (Exception ex) {
            token = "";
            ex.printStackTrace();
        }
        return token;
    }
}
