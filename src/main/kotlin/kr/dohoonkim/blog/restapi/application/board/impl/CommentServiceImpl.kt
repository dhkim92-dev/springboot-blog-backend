package kr.dohoonkim.blog.restapi.application.board.impl

//
//@Service
//class CommentServiceImpl(
//    private val commentRepository: CommentRepository,
//    private val articleRepository: ArticleRepository,
//    private val authenticationUtil: AuthenticationUtil
//){
//    @Transactional
//    fun createComment(articleId : UUID, request : CommentCreateRequest) : CommentDto {
//        val member = authenticationUtil.extractAuthenticationMember()
//                ?: throw UnauthorizedException(ErrorCode.AUTHENTICATION_FAIL)
//
//        val article = this.articleRepository.findByArticleId(articleId)
//                ?: throw EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND)
//
//        var parent : Comment? = request.parentId?.let{
//            it->this.commentRepository.findById(it).get()
//                ?: throw EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND)
//        }?:null
//
//        val comment = Comment(parent, request.contents, member, article)
//
//        return CommentDto.fromEntity(this.commentRepository.save(comment))
//    }
//
//    @Transactional
//    fun getCommentsInArticle(articleId : UUID) : CursorList<CommentDto> {
//        TODO("Not yet implemented")
//    }
//
//    @Transactional
//    fun getReplyComments(commentId : Long) : CursorList<CommentDto> {
//        TODO("Not yet implemented")
//    }
//
//    @Transactional
//    fun modifyCommentContents(commentId : Long, request : CommentContentsChangeRequest)
//    : CommentDto {
//        val member : Member = authenticationUtil.extractAuthenticationMember()
//                        ?: throw UnauthorizedException(ErrorCode.AUTHENTICATION_FAIL)
//
//        val comment : Comment = this.commentRepository.findById(commentId).get()
//                ?: throw EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND)
//
//        if(!comment.author.id.equals(member.id)) {
//            throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
//        }
//
//        return CommentDto.fromEntity(this.commentRepository.save(comment))
//    }
//
//    @Transactional
//    fun deleteComment(commentId : Long) {
//        val member = authenticationUtil.extractAuthenticationMember()
//                ?: throw UnauthorizedException(ErrorCode.AUTHENTICATION_FAIL)
//
//        val comment = this.commentRepository.findById(commentId).get()
//                ?: throw EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND)
//
//        if(member.id.equals(comment.author.id)) {
//            throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
//        }
//
//        this.commentRepository.deleteById(commentId)
//    }
//
//}